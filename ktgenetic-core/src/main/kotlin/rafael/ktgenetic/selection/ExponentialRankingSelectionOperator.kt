package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom
import kotlin.math.pow

private const val c = 0.975

/**
 * Code Based on http://jenetics.io/javadoc/jenetics/5.1/io/jenetics/ExponentialRankSelector.html and
 * https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/ExponentialRankSelector.java
 */
class ExponentialRankingSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
    SelectionOperator<C> {

    companion object {

        val rankingBySize = mutableMapOf<Int, List<Double>>()

        fun calculateRanking(n: Int): List<Double> {
            val b = (c - 1.0) / (c.pow(n.toDouble()) - 1)
            val a = n.toDouble() - 1

            return (0 until n).map { c.pow(a - it) * b }.reversed()
        }

    }


    private tailrec fun selectPosition(ranking: List<Double>, sortedValue: Double, pos: Int = 0): Int {
        if (pos >= ranking.size) {
            return ranking.size - 1
        }
        if (sortedValue <= 0) {
            return pos
        }
        return selectPosition(ranking, sortedValue - ranking[pos], pos + 1)
    }

    private fun selectElement(ranking: List<Double>, population: List<C>): C {
        val sortedValue = geneticRandom.nextDouble()
        val selectedPosition = selectPosition(ranking, sortedValue)
        return population[selectedPosition]
    }

    override fun select(children: List<C>): List<C> {
        val ranking = rankingBySize.computeIfAbsent(children.size) { calculateRanking(it) }

        return (0 until generationSize)
            .map { selectElement(ranking, children) }
            .sortedByDescending { it.fitness }
    }

    override fun toString(): String = selectorToString(this)

}