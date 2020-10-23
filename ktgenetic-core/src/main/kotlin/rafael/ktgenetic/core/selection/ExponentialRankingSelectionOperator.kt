package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import kotlin.math.pow
import kotlin.random.Random

private const val c = 0.975

/**
 * Code Based on http://jenetics.io/javadoc/jenetics/5.1/io/jenetics/ExponentialRankSelector.html and
 * https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/ExponentialRankSelector.java
 */
class ExponentialRankingSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    allowRepetition: Boolean
) :
    SelectionOperator<C> {

    companion object {

        val rankingBySize = mutableMapOf<Int, DoubleArray>()

        fun calculateRanking(n: Int): DoubleArray {
            val b = (c - 1.0) / (c.pow(n.toDouble()) - 1)
            val a = n.toDouble() - 1

            return DoubleArray(n) { c.pow(a - it) * b }.reversedArray()
        }

    }

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
     * Implementation Note: During the implementation of allow repetition, I perceived a problem when the selected
     * Collection is a Set. Its size took so long to reach the requested size that it seems it will never reach it.
     * Probably, it is related to the fact that the chromosomes with low ranking had so little chance (because of
     * exponential value) of being selected that were rarely selected. My workaround was to establish an arbitrary
     * maximum number of interactions.
     */
    private tailrec fun selectElements(
        ranking: DoubleArray,
        population: List<C>,
        interactions: Int,
        selected: MutableCollection<C>
    ): List<C> {
        if (selected.size == generationSize || interactions > (5 * generationSize)) {
            return selected.sortedBy { - it.fitness }
        }

        val sortedValue = Random.nextDouble()
        val selectedPosition = selectPosition(ranking, sortedValue)
        val selectedElement = population[selectedPosition]

        selected.add(selectedElement)
        return selectElements(ranking, population, interactions + 1, selected)
    }

    private val getInitialSelected: () -> MutableCollection<C> =
        if (allowRepetition) { ->  arrayListOf() } else { -> sortedSetOf() }

    override fun select(children: List<C>): List<C> {
        val ranking = rankingBySize.computeIfAbsent(children.size) { calculateRanking(it) }

        return selectElements(ranking, children, 1, getInitialSelected())
    }

    override fun toString(): String = selectorToString(this)

}
