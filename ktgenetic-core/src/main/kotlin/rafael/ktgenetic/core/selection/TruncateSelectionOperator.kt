package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome

/**
 * This class represents a Truncate Selection Operator used in genetic algorithms.
 * Truncate Selection is a method of selection where a certain portion of the worst individuals are discarded,
 * and the remaining individuals are left to breed.
 * More details can be found at:
 * - https://en.wikipedia.org/wiki/Truncation_selection
 * - https://watchmaker.uncommons.org/manual/ch03.html#d0e717
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. _If this value is `false`,
 * the Children size can be less than the generationSize_.
 *
 * @see SelectionOperator
 */
internal class TruncateSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
) :
    SelectionOperator<C> {

    /**
     * This companion object contains two methods for selecting chromosomes:
     * - listSelector: Selects chromosomes from a list, allowing repetition
     * - setSelector: Selects chromosomes from a set, not allowing repetition
     */
    companion object {

        /**
         * Selects chromosomes from a list, allowing repetition.
         * @param children The list of chromosomes
         * @param size The number of chromosomes to select
         * @return The selected chromosomes
         */
        fun <C : Chromosome<*>> listSelector(children: List<C>, size: Int): List<C> =
            children.sortedBy { -it.fitness }.subList(0, size)

        /**
         * Selects chromosomes from a set, not allowing repetition.
         * @param children The list of chromosomes
         * @param size The number of chromosomes to select
         * @return The selected chromosomes. _its size can be less than the size parameter_.
         */
        fun <C : Chromosome<*>> setSelector(children: List<C>, size: Int): List<C> {
            val sortedSet = children.toSortedSet(compareByDescending { it.fitness })
            val finalSize = if (sortedSet.size < size) sortedSet.size else size

            return sortedSet.toList().subList(0, finalSize)
        }

    }

    /**
     * The selector function to be used, determined by the allowRepetition property.
     */
    private val selector: (List<C>, Int) -> List<C> =
        if (allowRepetition) Companion::listSelector else Companion::setSelector

    /**
     * Selects a list of chromosomes from the given list of children.
     * @param children The list of chromosomes
     * @return The selected chromosomes
     */
    override fun select(children: List<C>): List<C> = selector(children, generationSize)

    /**
     * Returns a string representation of the TruncateSelectionOperator.
     * @return A string representation of the TruncateSelectionOperator
     */
    override fun toString(): String = selectorToString(this)

}