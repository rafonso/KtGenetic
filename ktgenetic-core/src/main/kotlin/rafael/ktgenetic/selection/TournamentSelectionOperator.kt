package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.createRandomPositions
import java.util.*

/**
 * https://en.wikipedia.org/wiki/Tournament_selection
 */
internal class TournamentSelectionOperator<C : Chromosome<*>>(override val generationSize: Int) :
    SelectionOperator<C> {

    private tailrec fun select(population: MutableList<C>, winners: List<C>): List<C> {
        if (winners.size >= generationSize) {
            return winners
        }

        val (pos1, pos2) = createRandomPositions(population.size)
        val winnerPosition = if (population[pos1].fitness > population[pos2].fitness) pos1 else pos2
        val winnerElement = population[winnerPosition]
        population.removeAt(winnerPosition)

        return select(population, winners + winnerElement)
    }

    override fun select(children: List<C>): List<C> =
            select(LinkedList(children) as MutableList<C>, listOf()).sortedBy { it.fitness }.reversed()

    override fun toString(): String = this.javaClass.simpleName

}