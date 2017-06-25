package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom

internal class RouletteElitismSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
        SelectionOperator<C> {

    val fittestSelectionFactor = 0.1
    val fittestChildrenToBeSaved = (fittestSelectionFactor * generationSize).toInt()

    tailrec fun selectPosition(children: List<C>, sortedValue: Double, pos: Int = 0): Int {
        if (pos >= children.size) {
            return children.size - 1
        }
        if (sortedValue <= 0.0) {
            return pos
        }

        return selectPosition(children, sortedValue - children[pos].fitness, pos + 1)
    }

    private tailrec fun selectElements(candidates: List<C>, remainingQuantity: Int, totalFitness: Double, selected: List<C> = listOf()): List<C> {
        if (remainingQuantity == 0) {
            return selected
        }

        val sortedValue = geneticRandom.nextDouble() * totalFitness
        val selectedPosition = selectPosition(candidates, sortedValue)
        val selectedElement = candidates[selectedPosition]

        return selectElements(candidates - selected, remainingQuantity - 1, totalFitness - selectedElement.fitness, selected + selectedElement)
    }

    override fun select(children: List<C>): List<C> {
        val sortedChildren = children.sortedBy { it.fitness }.reversed()
        val bestChildren = sortedChildren.subList(0, fittestChildrenToBeSaved)
        val remainingChildren = sortedChildren.subList(fittestChildrenToBeSaved, sortedChildren.size - 1)
        val totalFitness = remainingChildren.map { it.fitness }.sum()

        return bestChildren + selectElements(remainingChildren, generationSize - fittestChildrenToBeSaved, totalFitness)
    }

}