package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome

/**
 * https://en.wikipedia.org/wiki/Truncation_selection
 */
internal class TruncateSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
) :
    SelectionOperator<C> {

    companion object {

        fun <C : Chromosome<*>> listSelector(children: List<C>, size: Int): List<C> =
            children.sortedBy { -it.fitness }.subList(0, size)

        fun <C : Chromosome<*>> setSelector(children: List<C>, size: Int): List<C> {

            tailrec fun process(remainingChildren: MutableList<C>, selected: MutableSet<C>): List<C> {
                if ((selected.size == size) || remainingChildren.isEmpty()) {
                    return selected.toList()
                }

                val first = remainingChildren.removeFirst()
                selected.add(first)
                return process(remainingChildren, selected)
            }

            return process(ArrayDeque(children), linkedSetOf())
        }

    }

    private val selector: (List<C>, Int) -> List<C> =
        if (allowRepetition) Companion::listSelector else Companion::setSelector

    override fun select(children: List<C>): List<C> = selector(children, generationSize)

    override fun toString(): String = selectorToString(this)

}
