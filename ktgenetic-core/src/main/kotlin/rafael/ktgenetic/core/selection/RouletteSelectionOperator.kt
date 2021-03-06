package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import kotlin.random.Random

/**
 * See https://en.wikipedia.org/wiki/Fitness_proportionate_selection
 */
internal class RouletteSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
) : SelectionOperator<C> {

    private tailrec fun selectPosition(children: DoubleArray, sortedValue: Double, pos: Int = 0): Int {
        if (pos >= children.size) {
            return children.size - 1
        }
        if (sortedValue <= 0.0) {
            return pos
        }

        return selectPosition(children, sortedValue - children[pos], pos + 1)
    }

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

    override fun select(children: List<C>): List<C> {
        val fitnesses = children.map { it.fitness }.toDoubleArray()
        val totalFitness = fitnesses.sum()
        val selected: MutableCollection<C> = if (allowRepetition) arrayListOf() else sortedSetOf()

        return selectElements(ArrayList(children), fitnesses, generationSize, totalFitness, selected)
    }

    override fun toString(): String = selectorToString(this)

}
