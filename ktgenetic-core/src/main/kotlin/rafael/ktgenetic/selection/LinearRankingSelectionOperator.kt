package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom
import java.util.*
import kotlin.collections.ArrayList

private const val nMin = 0.9
private const val nMax = 2.0 - nMin

/**
 * Code Based on http://jenetics.io/javadoc/jenetics/5.1/io/jenetics/LinearRankSelector.html and
 * https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/LinearRankSelector.java
 */
class LinearRankingSelectionOperator<C : Chromosome<*>>(override val generationSize: Int, override val allowRepetition: Boolean) :
    SelectionOperator<C> {

    companion object {

        val rankingBySize = mutableMapOf<Int, DoubleArray>()

        fun calculateRanking(n: Int) = DoubleArray(n) {(nMin + (nMax - nMin) * (n - it - 1) / (n - 1)) }

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

    private tailrec fun selectElements(
        ranking: DoubleArray,
        population: List<C>,
        selected: MutableCollection<C>
    ): List<C> {
        if (selected.size == generationSize) {
            return selected.sortedBy { - it.fitness }
        }

        val sortedValue = geneticRandom.nextInt(population.size).toDouble()
        val selectedPosition = selectPosition(ranking, sortedValue)
        val selectedElement = population[selectedPosition]

        selected.add(selectedElement)
        return selectElements(ranking, population, selected)
    }

    private val getInitialSelected: () -> MutableCollection<C> =
            if (allowRepetition) { -> ArrayList() } else { -> TreeSet() }

    override fun select(children: List<C>): List<C> {
        assert(allowRepetition || children.size > generationSize) {
            "children size (${children.size}) must be greater than Generation Size ($generationSize) "
        }

        val ranking = rankingBySize.computeIfAbsent(children.size) { calculateRanking(it) }

        return selectElements(ranking, children, getInitialSelected())
    }

    override fun toString(): String = selectorToString(this)

}
