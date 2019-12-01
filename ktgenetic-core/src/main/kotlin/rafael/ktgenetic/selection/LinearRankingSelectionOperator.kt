package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom

private const val nMin = 0.9
private const val nMax = 2.0 - nMin

/**
 * Code Based on http://jenetics.io/javadoc/jenetics/5.1/io/jenetics/LinearRankSelector.html and
 * https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/LinearRankSelector.java
 */
class LinearRankingSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
    SelectionOperator<C> {

    companion object {

        val rankingBySize = mutableMapOf<Int, List<Double>>()

        fun calculateRanking(n: Int) = (0 until n).map { (nMin + (nMax - nMin) * (n - it - 1) / (n - 1)) }

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
        val sortedValue = geneticRandom.nextInt(population.size).toDouble()
        val selectedPosition = selectPosition(ranking, sortedValue)
        return population[selectedPosition]
    }

    override fun select(children: List<C>): List<C> {
        val ranking = rankingBySize.computeIfAbsent(children.size) { calculateRanking(it) }

        return (0 until generationSize)
            .map { selectElement(ranking, children) }
            .sortedByDescending { it.fitness }
    }

    override fun toString(): String = "${this.javaClass.simpleName}($generationSize)"

}