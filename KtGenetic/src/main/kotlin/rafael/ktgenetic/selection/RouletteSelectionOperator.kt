package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom
import rafael.ktgenetic.mainLogger
import rafael.ktgenetic.makeCuttingIntoPieces
import java.util.*

internal class RouletteSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
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

    private tailrec fun selectElements(population: MutableList<C>, remainingQuantity: Int, totalFitness: Double, selected: List<C> = listOf()): List<C> {
        if (remainingQuantity == 0) {
            return selected
        }

        val sortedValue = geneticRandom.nextDouble() * totalFitness
        val selectedPosition = selectPosition(population, sortedValue)
        val selectedElement = population[selectedPosition]
        population.removeAt(selectedPosition)

        return selectElements(population, remainingQuantity - 1,
                totalFitness - selectedElement.fitness, selected + selectedElement)
    }

    override fun select(children: List<C>): List<C> {
        val totalFitness = children.map { it.fitness }.sum()

        return selectElements(ArrayList(children), generationSize, totalFitness)
    }

    override fun toString(): String = this.javaClass.simpleName

}