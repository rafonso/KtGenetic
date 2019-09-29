package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import kotlin.reflect.KFunction

internal class ElitismDelegateSelectionOperator<C : Chromosome<*>>(delegatedClass: KFunction<*>,
                                                                   override val generationSize: Int) :
        SelectionOperator<C> {

    private val eliteChildrenSize: Int = Math.max ((0.1 * generationSize).toInt(), 1)
    private val delegatedSelector: SelectionOperator<C>

    init {
        val remainingChildrenSize: Int = generationSize - eliteChildrenSize
        @Suppress("UNCHECKED_CAST")
        delegatedSelector = delegatedClass.call(remainingChildrenSize) as SelectionOperator<C>
    }

    override fun select(children: List<C>): List<C> {
        val eliteChildren = children.subList(0, eliteChildrenSize).toSet().toList()
        val remainingChildren = children.subList(eliteChildren.size, children.size)

        val remainingSurvivors = delegatedSelector.select(remainingChildren)

        return (eliteChildren + remainingSurvivors).sortedBy { it.fitness }.reversed()
    }

    override fun toString(): String = "Elitism[$delegatedSelector, elite = $eliteChildrenSize]"

}