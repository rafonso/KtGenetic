package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

internal class TruncateSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
        SelectionOperator<C> {

    override fun select(children: List<C>): List<C> = children.
            subList(0, generationSize)

}