package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import kotlin.random.Random

private const val nMin = 0.9
private const val nMax = 2.0 - nMin

/**
 * This class represents a Linear Ranking Selection Operator used in genetic algorithms.
 * Linear Ranking Selection is a method of selection where the probability of an individual being selected is proportional to its rank.
 * More details can be found at:
 * - [http://jenetics.io/javadoc/jenetics/5.1/io/jenetics/LinearRankSelector.html]
 * - [https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/LinearRankSelector.java]
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. If this value is false,
 * the Children size can be less than the generationSize.
 *
 * @see SelectionOperator
 */
class LinearRankingSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
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
        fun calculateRanking(n: Int) = DoubleArray(n) { (nMin + (nMax - nMin) * (n - it - 1) / (n - 1)) }

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
     * @param ranking The ranking
     * @param population The list of chromosomes
     * @param selected The collection of selected chromosomes
     * @return The selected chromosomes
     */
    private tailrec fun selectElements(
        ranking: DoubleArray,
        population: List<C>,
        selected: MutableCollection<C>
    ): List<C> {
        if (selected.size == generationSize) {
            return selected.sortedBy { -it.fitness }
        }

        val sortedValue = Random.nextInt(population.size).toDouble()
        val selectedPosition = selectPosition(ranking, sortedValue)
        val selectedElement = population[selectedPosition]

        selected.add(selectedElement)
        return selectElements(ranking, population, selected)
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
        assert(allowRepetition || children.size > generationSize) {
            "children size (${children.size}) must be greater than Generation Size ($generationSize) "
        }

        val ranking = rankingBySize.computeIfAbsent(children.size) { calculateRanking(it) }

        return selectElements(ranking, children, getInitialSelected())
    }

    /**
     * Returns a string representation of the LinearRankingSelectionOperator.
     * @return A string representation of the LinearRankingSelectionOperator
     */
    override fun toString(): String = selectorToString(this)

}
