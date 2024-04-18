package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import kotlin.random.Random

/**
 * This class represents a Roulette Selection Operator used in genetic algorithms.
 * Roulette Selection is a method of selection where the probability of an individual being selected is proportional to its fitness.
 * More details can be found at:
 * - https://en.wikipedia.org/wiki/Fitness_proportionate_selection
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. If this value is false,
 * the Children size can be less than the generationSize.
 *
 * @see SelectionOperator
 */
internal class RouletteSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
) : SelectionOperator<C> {

    /**
     * Recursively selects a position from the given list of fitnesses and a sorted value.
     * @param children The list of fitnesses
     * @param sortedValue The sorted value
     * @param pos The current position (default is 0)
     * @return The selected position
     */
    private tailrec fun selectPosition(children: DoubleArray, sortedValue: Double, pos: Int = 0): Int {
        if (pos >= children.size) {
            return children.lastIndex
        }
        if (sortedValue <= 0.0) {
            return pos
        }

        return selectPosition(children, sortedValue - children[pos], pos + 1)
    }

    /**
     * Recursively selects elements from the given population and fitnesses.
     * @param population The list of chromosomes
     * @param fitnesses The list of fitnesses
     * @param remainingQuantity The remaining quantity of elements to select
     * @param totalFitness The total fitness of the population
     * @param selected The collection of selected chromosomes
     * @return The selected chromosomes
     */
    private tailrec fun selectElements(
        population: MutableList<C>,
        fitnesses: DoubleArray,
        remainingQuantity: Int,
        totalFitness: Double,
        selected: MutableCollection<C>
    ): List<C> {
        if (remainingQuantity == 0) {
            return selected.toList().sortedBy { -it.fitness }
        }

        val sortedValue = Random.nextDouble() * totalFitness
        val selectedPosition = selectPosition(fitnesses, sortedValue)
        val selectedElement = population[selectedPosition]
        fitnesses[selectedPosition] = 0.0

        selected.add(selectedElement)
        return selectElements(
            population, fitnesses,
            remainingQuantity - 1, totalFitness - selectedElement.fitness, selected
        )
    }

    /**
     * Selects a list of chromosomes from the given list of children.
     * @param children The list of chromosomes
     * @return The selected chromosomes
     */
    override fun select(children: List<C>): List<C> {
        val fitnesses = children.map { it.fitness }.toDoubleArray()
        val totalFitness = fitnesses.sum()
        val selected: MutableCollection<C> = if (allowRepetition) arrayListOf() else sortedSetOf()

        return selectElements(ArrayList(children), fitnesses, generationSize, totalFitness, selected)
    }

    /**
     * Returns a string representation of the RouletteSelectionOperator.
     * @return A string representation of the RouletteSelectionOperator
     */
    override fun toString(): String = selectorToString(this)

}
