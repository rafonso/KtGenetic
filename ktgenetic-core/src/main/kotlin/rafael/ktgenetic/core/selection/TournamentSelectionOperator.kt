package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.utils.createRandomPositions

/**
 * This class represents a Tournament Selection Operator used in genetic algorithms.
 * Tournament Selection is a method of selection where a subset of individuals are chosen (a tournament),
 * and the individual with the highest fitness in the group is selected.
 * More details can be found at:
 * - [Wikipedia - Tournament selection](https://en.wikipedia.org/wiki/Tournament_selection)
 *
 * @param <C> The type of the Chromosome
 * @property generationSize The size of the generation to be created
 * @property allowRepetition If the same Chromosome can be selected more than once. If this value is false,
 * the Children size can be less than the generationSize.
 *
 * @see SelectionOperator
 */
internal class TournamentSelectionOperator<C : Chromosome<*>>(
    override val generationSize: Int,
    override val allowRepetition: Boolean
) :
    SelectionOperator<C> {

    /**
     * Recursively selects chromosomes from the given population until the desired generation size is reached.
     * @param population The list of chromosomes
     * @param selected The collection of selected chromosomes
     * @return The selected chromosomes
     */
    private tailrec fun select(population: MutableList<C>, selected: MutableCollection<C>): List<C> {
        if (selected.size >= generationSize) {
            return selected.toList().sortedBy { -it.fitness }
        }
        if (population.size == 2) {
            return (selected.toList() + population).sortedBy { -it.fitness }
        }

        val (pos1, pos2) = createRandomPositions(population.size)
        val winnerPosition = if (population[pos1].fitness > population[pos2].fitness) pos1 else pos2
        val winnerElement = population[winnerPosition]
        population.removeAt(winnerPosition)

        selected.add(winnerElement)
        return select(population, selected)
    }

    /**
     * Returns a mutable collection of chromosomes. The type of collection depends on whether repetition is allowed.
     */
    private val getWinners: () -> MutableCollection<C> =
        if (allowRepetition) { -> arrayListOf() } else { -> sortedSetOf() }

    /**
     * Selects a list of chromosomes from the given list of children.
     * @param children The list of chromosomes
     * @return The selected chromosomes
     */
    override fun select(children: List<C>): List<C> {
        assert(allowRepetition || children.size > generationSize) {
            "children size (${children.size}) must be greater than Generation Size ($generationSize) "
        }

        return select(ArrayList(children), getWinners())
    }

    /**
     * Returns a string representation of the TournamentSelectionOperator.
     * @return A string representation of the TournamentSelectionOperator
     */
    override fun toString(): String = selectorToString(this)

}
