package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

internal class ElitismDelegeteSelectionOperator<C : Chromosome<*>>(private val delegated: SelectionOperator<C>,
                                                                   override val generationSize: Int) :
        SelectionOperator<C> {

    private val bestChildrenSize: Int = (0.1 * generationSize).toInt()

    override fun select(children: List<C>): List<C> {
        val bestChildren = children.subList(0, bestChildrenSize)
        val remainingChildren = children.subList(bestChildrenSize, children.size - 1)

        val remainingSurvivors = delegated.select(remainingChildren)

        return (bestChildren + remainingSurvivors).sortedBy { it.fitness }.reversed()
    }

}