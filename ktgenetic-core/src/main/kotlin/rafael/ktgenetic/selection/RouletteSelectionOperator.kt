package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom
import java.util.*
import kotlin.collections.ArrayList

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
            return selected.toList().sortedBy { it.fitness }.reversed()
        }

        val sortedValue = geneticRandom.nextDouble() * totalFitness
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
        val totalFitness = children.map { it.fitness }.sum()
        val selected: MutableCollection<C> = if (allowRepetition) ArrayList() else TreeSet()
        val fitnesses = children.map { it.fitness }.toDoubleArray()

        return selectElements(ArrayList(children), fitnesses, generationSize, totalFitness, selected)
    }

    override fun toString(): String = selectorToString(this)

}
