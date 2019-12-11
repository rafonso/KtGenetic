package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import kotlin.math.max
import kotlin.reflect.KFunction

internal class ElitismDelegateSelectionOperator<C : Chromosome<*>>(
    delegatedClass: KFunction<*>,
    override val generationSize: Int,
    // TODO: Transformar em not null
    override val allowRepetition: Boolean? = null
) :
    SelectionOperator<C> {

    private val eliteChildrenSize: Int = max((0.1 * generationSize).toInt(), 1)
    private val delegatedSelector: SelectionOperator<C>

    init {
        val remainingChildrenSize: Int = generationSize - eliteChildrenSize
        @Suppress("UNCHECKED_CAST")
        delegatedSelector = if (allowRepetition == null)
            delegatedClass.call(remainingChildrenSize) as SelectionOperator<C>
        else
            delegatedClass.call(remainingChildrenSize, allowRepetition) as SelectionOperator<C>
    }

    override fun select(children: List<C>): List<C> {
        val eliteChildren = children.subList(0, eliteChildrenSize).toSet().toList()
        val remainingChildren = children.subList(eliteChildren.size, children.size)

        val remainingSurvivors = delegatedSelector.select(remainingChildren)

        return (eliteChildren + remainingSurvivors).sortedBy { it.fitness }.reversed()
    }

    override fun toString(): String = "Elitism[$delegatedSelector, elite = $eliteChildrenSize]"

}