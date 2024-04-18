package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome

/**
 * This interface represents a Selection Operator used in genetic algorithms.
 * A Selection Operator is responsible for selecting a subset of a population for reproduction.
 * More details can be found at:
 * - [Wikipedia - Selection](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm))
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. If this value is false,
 * the Children size can be less than the generationSize.
 */
interface SelectionOperator<C : Chromosome<*>> {

    /**
     * The size of the generation to be created.
     */
    val generationSize: Int

    /**
     * If the same Chromosome can be selected more than once. If this value is false,
     * the Children size can be less than the generationSize.
     */
    // TODO: Transformar em not null
    @Suppress("SameReturnValue")
    val allowRepetition: Boolean?
        get() = false

    /**
     * Selects a list of chromosomes from the given list of children.
     * @param children The list of chromosomes
     * @return The selected chromosomes
     */
    fun select(children: List<C>): List<C>

}

/**
 * Returns a string representation of the SelectionOperator.
 * @param selector The SelectionOperator
 * @return A string representation of the SelectionOperator
 */
internal fun selectorToString(selector: SelectionOperator<*>) =
        "${selector.javaClass.simpleName}(${selector.generationSize}, ${selector.allowRepetition})"
