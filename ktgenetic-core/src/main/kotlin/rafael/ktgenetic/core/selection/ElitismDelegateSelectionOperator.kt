package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import kotlin.math.max
import kotlin.reflect.KFunction

/**
 * This class represents an Elitism Delegate Selection Operator used in genetic algorithms.
 * Elitism Delegate Selection Operator is a method of selection where a certain percentage of the best fit individuals are guaranteed to be selected.
 * The remaining individuals are selected by a delegated selection operator.
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. If this value is false,
 * the Children size can be less than the generationSize.
 * @property delegatedClass The class of the delegated selection operator
 * @property eliteChildrenSize The number of best fit individuals guaranteed to be selected
 * @property delegatedSelector The delegated selection operator
 *
 * @see SelectionOperator
 */
internal class ElitismDelegateSelectionOperator<C : Chromosome<*>>(
    delegatedClass: KFunction<*>,
    override val generationSize: Int,
    override val allowRepetition: Boolean
) :
    SelectionOperator<C> {

    private val eliteChildrenSize: Int = max((0.1 * generationSize).toInt(), 1)
    private val delegatedSelector: SelectionOperator<C>

    init {
        val remainingChildrenSize: Int = generationSize - eliteChildrenSize
        @Suppress("UNCHECKED_CAST")
        delegatedSelector = delegatedClass.call(remainingChildrenSize, allowRepetition) as SelectionOperator<C>
    }

    /**
     * Selects a list of chromosomes from the given list of children.
     * The best fit individuals are guaranteed to be selected, and the remaining individuals are selected by the delegated selection operator.
     * @param children The list of chromosomes
     * @return The selected chromosomes
     */
    override fun select(children: List<C>): List<C> {
        val eliteChildren = children.subList(0, eliteChildrenSize).toSet().toList()
        val remainingChildren = children.subList(eliteChildren.size, children.size)

        val remainingSurvivors = delegatedSelector.select(remainingChildren)

        return (eliteChildren + remainingSurvivors).sortedBy { it.fitness }.reversed()
    }

    /**
     * Returns a string representation of the ElitismDelegateSelectionOperator.
     * @return A string representation of the ElitismDelegateSelectionOperator
     */
    override fun toString(): String = "Elitism[$delegatedSelector, elite = $eliteChildrenSize]"

}
