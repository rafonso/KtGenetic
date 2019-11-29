package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom

data class LinearProbabilities(val n: Int) {

    val total = n * (n + 1) / 2

    val ranking = (0 until n).map { n - it }

}

class LinearRankingSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
    SelectionOperator<C> {

    companion object {

        val probabilitiesBySize = mutableMapOf<Int, LinearProbabilities>()

    }

    private fun selectPosition(ranking: List<Int>, sortedValue: Int, pos: Int = 0): Int {
        if (pos >= ranking.size) {
            return ranking.size - 1
        }
        if (sortedValue <= 0) {
            return pos
        }
        return selectPosition(ranking, sortedValue - ranking[pos], pos + 1)
    }

    private fun selectElement(probabilitiesSize: LinearProbabilities, population: List<C>): C {
        val sortedValue = geneticRandom.nextInt(probabilitiesSize.total)
        val selectedPosition = selectPosition(probabilitiesSize.ranking, sortedValue)
        return population[selectedPosition]
    }

    override fun select(children: List<C>): List<C> {
        val probabilitiesSize = probabilitiesBySize.computeIfAbsent(children.size) { LinearProbabilities(it) }

        return (0 until generationSize).map { selectElement(probabilitiesSize, children) }
            .sortedByDescending { it.fitness }
    }

    override fun toString(): String = "${this.javaClass.simpleName}($generationSize)"

}