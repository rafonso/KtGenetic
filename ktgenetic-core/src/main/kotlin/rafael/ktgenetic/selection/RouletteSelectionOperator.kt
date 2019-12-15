package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom
import java.util.*
import kotlin.collections.ArrayList

internal class RouletteSelectionOperator<C : Chromosome<*>>(override val generationSize: Int,
                                                            override val allowRepetition: Boolean) :
        SelectionOperator<C> {

    private tailrec fun selectPosition(children: List<C>, sortedValue: Double, pos: Int = 0): Int {
        if (pos >= children.size) {
            return children.size - 1
        }
        if (sortedValue <= 0.0) {
            return pos
        }

        return selectPosition(children, sortedValue - children[pos].fitness, pos + 1)
    }

    private tailrec fun selectElements(population: MutableList<C>, remainingQuantity: Int, totalFitness: Double, selected: MutableCollection<C>): List<C> {
        if (remainingQuantity == 0) {
            return selected.toList().sortedBy { it.fitness }.reversed()
        }

        val sortedValue = geneticRandom.nextDouble() * totalFitness
        val selectedPosition = selectPosition(population, sortedValue)
        val selectedElement = population[selectedPosition]
        population.removeAt(selectedPosition)

        selected.add(selectedElement)
        return selectElements(population, remainingQuantity - 1,
                totalFitness - selectedElement.fitness, selected)
    }

    override fun select(children: List<C>): List<C> {
        val totalFitness = children.map { it.fitness }.sum()
        val selected: MutableCollection<C> = if (allowRepetition) ArrayList() else TreeSet()

        return selectElements(ArrayList(children), generationSize, totalFitness, selected)
    }

    override fun toString(): String = selectorToString(this)

}
