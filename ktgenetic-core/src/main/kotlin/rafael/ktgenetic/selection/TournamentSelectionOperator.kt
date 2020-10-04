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
    override val allowRepetition: Boolean
) :
    SelectionOperator<C> {

    private tailrec fun select(population: MutableList<C>, selected: MutableCollection<C>): List<C> {
        if (selected.size >= generationSize) {
            return selected.toList().sortedBy { it.fitness }.reversed()
        }
        if(population.size == 2) {
            return (selected.toList() + population).sortedBy { it.fitness }.reversed()
        }

        val (pos1, pos2) = createRandomPositions(population.size)
        val winnerPosition = if (population[pos1].fitness > population[pos2].fitness) pos1 else pos2
        val winnerElement = population[winnerPosition]
        population.removeAt(winnerPosition)

        selected.add(winnerElement)
        return select(population, selected)
    }

    private val getWinners: () -> MutableCollection<C> = if (allowRepetition) { -> ArrayList() } else { -> TreeSet() }

    override fun select(children: List<C>): List<C> {
        assert(allowRepetition || children.size > generationSize) {
            "children size (${children.size}) must be greater than Generation Size ($generationSize) "
        }

        return select(LinkedList(children) as MutableList<C>, getWinners())
    }

    override fun toString(): String = selectorToString(this)

}
