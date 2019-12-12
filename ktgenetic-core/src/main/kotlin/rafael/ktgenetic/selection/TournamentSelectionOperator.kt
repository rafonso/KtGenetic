package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.createRandomPositions
import java.util.*
import kotlin.collections.ArrayList

/**
 * https://en.wikipedia.org/wiki/Tournament_selection
 */
internal class TournamentSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    allowRepetition: Boolean
) :
    SelectionOperator<C> {

    private tailrec fun select(population: MutableList<C>, winners: MutableCollection<C>): List<C> {
        if (winners.size >= generationSize) {
            return winners.toList()
        }
        if(population.size == 2) {
            return winners.toList() + population
        }

        val (pos1, pos2) = createRandomPositions(population.size)
        val winnerPosition = if (population[pos1].fitness > population[pos2].fitness) pos1 else pos2
        val winnerElement = population[winnerPosition]
        population.removeAt(winnerPosition)

        winners.add(winnerElement)
        return select(population, winners)
    }

    private val getWinners: () -> MutableCollection<C> = if (allowRepetition) { -> ArrayList() } else { -> TreeSet() }

    override fun select(children: List<C>): List<C> = select(LinkedList(children) as MutableList<C>, getWinners())

    override fun toString(): String = selectorToString(this)

}
