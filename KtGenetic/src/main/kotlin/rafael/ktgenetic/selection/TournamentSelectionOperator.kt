package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.geneticRandom

internal class TournamentSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) : SelectionOperator<C> {

    private fun select(population: List<C>, winners: List<C>): List<C> {
        if (winners.size >= generationSize) {
            return winners
        }

        val candidate1 = population[geneticRandom.nextInt(population.size)]
        val candidate2 = population[geneticRandom.nextInt(population.size)]
        val winner = if (candidate1.fitness > candidate2.fitness) candidate1 else candidate2

        return select(population, winners + winner)
    }

    override fun select(children: List<C>): List<C> = select(children, listOf()).sortedBy { it.fitness }.reversed()

}