package rafael.ktgenetic.core.processor

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.core.selection.SelectionOperator
import rafael.ktgenetic.core.utils.pFlatMap
import rafael.ktgenetic.core.utils.pForEach

/**
 * Executes the evolutionary process.
 *
 * @param crossingType Crossing Type
 * @param environment Environment with selection rules
 * @param selectionOperator [SelectionOperator] to be used
 *
 *
 * @property environment The environment of the genetic algorithm
 * @property selectionOperator The selection operator to be used
 * @property geneticCrosser The genetic crosser to be used
 * @property listeners The set of processor listeners
 * @property continueProcessing A flag indicating whether to continue processing
 */
class GeneticProcessor<G, C : Chromosome<G>>(
    crossingType: GeneticCrossingType,
    val environment: Environment<G, C>,
    private val selectionOperator: SelectionOperator<C>
) {

    private val geneticCrosser = crossingType.getGeneticCrosser<G, C>()

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    var continueProcessing = true

    /**
     * Notifies all listeners of a given event.
     * @param event The event to notify
     */
    private fun notifyEvent(event: ProcessorEvent<*>) {
        listeners.parallelStream().forEach { it.onEvent(event) }
    }

    /**
     * Notifies all listeners of a given event type, generation, and population.
     * @param eventType The type of the event
     * @param generation The generation number
     * @param population The population of chromosomes
     */
    private fun notifyEvent(eventType: TypeProcessorEvent, generation: Int, population: List<C>) {
        notifyEvent(ProcessorEvent(eventType, generation, population))
    }

    /**
     * Executes mutation on a given chromosome.
     * @param chromosome The chromosome to mutate
     * @return The mutated chromosome
     */
    private fun executeMutation(chromosome: C): C =
        if (Math.random() < environment.mutationFactor) environment.createNewChromosome(
            environment.executeMutation(chromosome.content)
        )
        else chromosome

    /**
     * Crosses two parent chromosomes.
     * @param parent1 The first parent chromosome
     * @param parent2 The second parent chromosome
     * @return The list of offspring chromosomes
     */
    private fun cross(parent1: C, parent2: C): List<C> {
        val cutPositions = environment.getCutPositions()

        val pieces1 = environment.cutIntoPieces(parent1.content, cutPositions)
        val pieces2 = environment.cutIntoPieces(parent2.content, cutPositions)

        return geneticCrosser.executeCrossing(pieces1, pieces2, environment)
    }

    /**
     * Infers the end processing type based on the generation number.
     * @param generation The generation number
     * @return The inferred end processing type
     */
    private fun inferEndProcessingType(generation: Int): TypeProcessorEvent =
        if (!continueProcessing) {
            TypeProcessorEvent.ENDED_BY_INTERRUPTION
        } else if (generation <= environment.maxGenerations) {
            TypeProcessorEvent.ENDED_BY_FITNESS
        } else {
            TypeProcessorEvent.ENDED_BY_GENERATIONS
        }

    /**
     * Processes a generation of chromosomes.
     * @param generation The generation number
     * @param parents The list of parent chromosomes
     * @return The processor event
     */
    private tailrec fun processGeneration(generation: Int, parents: List<C>): ProcessorEvent<C> {
        if (!continueProcessing || environment.resultFound(parents) || (generation > environment.maxGenerations)) {
            return ProcessorEvent(inferEndProcessingType(generation), generation - 1, parents)
        }

        val selected = processPopulation(generation, parents)

        return processGeneration(generation + 1, selected)
    }

    /**
     * Processes a population of chromosomes.
     * @param generation The generation number
     * @param parents The list of parent chromosomes
     * @return The list of selected chromosomes
     */
    private fun processPopulation(generation: Int, parents: List<C>): List<C> {
        try {
            notifyEvent(TypeProcessorEvent.GENERATION_EVALUATING, generation, parents)

            notifyEvent(TypeProcessorEvent.REPRODUCING, generation, parents)
            val children = (parents.indices).pFlatMap { i ->
                ((i + 1) until parents.size).pFlatMap { j ->
                    notifyEvent(TypeProcessorEvent.CROSSING, generation, listOf(parents[i], parents[j]))
                    cross(parents[i], parents[j]).also {
                        notifyEvent(TypeProcessorEvent.CROSSED, generation, it)
                    }
                }
            } + parents

            notifyEvent(TypeProcessorEvent.MUTATION_EXECUTING, generation, children)
            val mutated = children.map { executeMutation(it) }

            notifyEvent(TypeProcessorEvent.FITNESS_CALCULATING, generation, mutated)
            // Calculate Fitness
            mutated.pForEach {
                it.fitness = environment.calculateFitness(it)
            }

            notifyEvent(TypeProcessorEvent.SELECTING, generation, mutated)
            val selected = selectionOperator.select(mutated.sortedBy { -it.fitness })

            notifyEvent(TypeProcessorEvent.GENERATION_EVALUATED, generation, selected)
            return selected
        } catch (e: Exception) {
            notifyEvent(ProcessorEvent(TypeProcessorEvent.ERROR, generation, parents, e))
            throw e
        }
    }


    /**
     * Starts the genetic process.
     * @return The processor event
     */
    fun process(): ProcessorEvent<C> {
        notifyEvent(TypeProcessorEvent.STARTING, environment.maxGenerations, listOf())

        notifyEvent(TypeProcessorEvent.FIRST_GENERATION_CREATING, 0, listOf())
        val population = environment.getFirstGeneration()

        return processGeneration(1, population).also { notifyEvent(it) }
    }

    /**
     * Stops the genetic process.
     */
    fun stop() {
        continueProcessing = false
    }

    /**
     * Adds a listener to the processor.
     * @param listener The listener to add
     * @return True if the listener was added, false otherwise
     */
    fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    /**
     * Removes a listener from the processor.
     * @param listener The listener to remove
     * @return True if the listener was removed, false otherwise
     */
    @Suppress("unused")
    fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}
