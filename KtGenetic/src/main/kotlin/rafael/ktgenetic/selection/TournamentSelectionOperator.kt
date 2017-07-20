package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.createRandomPositions
import rafael.ktgenetic.makeCuttingIntoPieces

internal class TournamentSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
        SelectionOperator<C> {

    private tailrec fun select(population: List<C>, winners: List<C>): List<C> {
        if (winners.size >= generationSize) {
            return winners
        }

        val (pos1, pos2) = createRandomPositions(population.size)
        val winnerPos = if (population[pos1].fitness > population[pos2].fitness) pos1 else pos2
        val (first, _, second) = makeCuttingIntoPieces(population, Pair(winnerPos, winnerPos + 1))

        return select(first + second, winners + population[winnerPos])
    }

    override fun select(children: List<C>): List<C> = select(children, listOf()).sortedBy { it.fitness }.reversed()

}