package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import kotlin.math.pow
import kotlin.random.Random

private const val c = 0.975

/**
 * This class represents an Exponential Ranking Selection Operator used in genetic algorithms.
 * Exponential Ranking Selection is a method of selection where the probability of an individual being selected is proportional to its rank, with an exponential distribution.
 * More details can be found at:
 * - http://jenetics.io/javadoc/jenetics/5.1/io/jenetics/ExponentialRankSelector.html
 * - https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/ExponentialRankSelector.java
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. If this value is false,
 * the Children size can be less than the generationSize.
 *
 * @see SelectionOperator
 */
class ExponentialRankingSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    allowRepetition: Boolean
) :
    SelectionOperator<C> {

    companion object {

        /**
         * A map that stores the ranking for each size of population.
         */
        val rankingBySize = mutableMapOf<Int, DoubleArray>()

        /**
         * Calculates the ranking for a given size of population.
         * @param n The size of the population
         * @return The ranking for the population
         */
        fun calculateRanking(n: Int): DoubleArray {
            val b = (c - 1.0) / (c.pow(n.toDouble()) - 1)
            val a = n.toDouble() - 1

            return DoubleArray(n) { c.pow(a - it) * b }.reversedArray()
        }

    }

    /**
     * Recursively selects a position from the given ranking and a sorted value.
     * @param ranking The ranking
     * @param sortedValue The sorted value
     * @param pos The current position (default is 0)
     * @return The selected position
     */
    private tailrec fun selectPosition(ranking: DoubleArray, sortedValue: Double, pos: Int = 0): Int {
        if (pos >= ranking.size) {
            return ranking.size - 1
        }
        if (sortedValue <= 0) {
            return pos
        }
        return selectPosition(ranking, sortedValue - ranking[pos], pos + 1)
    }

    /**
     * Recursively selects elements from the given ranking and population.
     *
     * __Implementation Note__: During the implementation of allow repetition, I perceived a problem when the selected
     * Collection is a Set. Its size took so long to reach the requested size that it seems it will never reach it.
     * Probably, it is related to the fact that the chromosomes with low ranking had so little chance (because of
     * exponential value) of being selected that were rarely selected. My workaround was to establish an arbitrary
     * maximum number of interactions.
     *
     * @param ranking The ranking
     * @param population The list of chromosomes
     * @param interactions The number of interactions
     * @param selected The collection of selected chromosomes
     * @return The selected chromosomes
     */
    private tailrec fun selectElements(
        ranking: DoubleArray,
        population: List<C>,
        interactions: Int,
        selected: MutableCollection<C>
    ): List<C> {
        if (selected.size == generationSize || interactions > (5 * generationSize)) {
            return selected.sortedBy { -it.fitness }
        }

        val sortedValue = Random.nextDouble()
        val selectedPosition = selectPosition(ranking, sortedValue)
        val selectedElement = population[selectedPosition]

        selected.add(selectedElement)
        return selectElements(ranking, population, interactions + 1, selected)
    }

    /**
     * Returns a mutable collection of chromosomes. The type of collection depends on whether repetition is allowed.
     */
    private val getInitialSelected: () -> MutableCollection<C> =
        if (allowRepetition) { -> arrayListOf() } else { -> sortedSetOf() }

    /**
     * Selects a list of chromosomes from the given list of children.
     * @param children The list of chromosomes
     * @return The selected chromosomes
     */
    override fun select(children: List<C>): List<C> {
        val ranking = rankingBySize.computeIfAbsent(children.size) { calculateRanking(it) }

        return selectElements(ranking, children, 1, getInitialSelected())
    }

    /**
     * Returns a string representation of the ExponentialRankingSelectionOperator.
     * @return A string representation of the ExponentialRankingSelectionOperator
     */
    override fun toString(): String = selectorToString(this)

}
