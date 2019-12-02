package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import java.util.*

/**
 * https://en.wikipedia.org/wiki/Truncation_selection
 */
internal class TruncateSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
) :
    SelectionOperator<C> {

    companion object {

        fun <C : Chromosome<*>> listSelector(children: List<C>, size: Int): List<C> = children.subList(0, size)

        fun <C : Chromosome<*>> setSelector(children: List<C>, size: Int): List<C> {

            tailrec fun process(remainingChildren: LinkedList<C>, selected: MutableSet<C>): List<C> {
                if ((selected.size == size) || remainingChildren.isEmpty()) {
                    return selected.toList().reversed()
                }

                val first = remainingChildren.removeFirst()
                selected.add(first)
                return process(remainingChildren, selected)
            }

            return process(LinkedList(children), TreeSet())
        }

    }

    private val selector: (List<C>, Int) -> List<C> = if (allowRepetition) ::listSelector else ::setSelector

    override fun select(children: List<C>): List<C> = selector(children, generationSize)

    override fun toString(): String = selectorToString(this)

}