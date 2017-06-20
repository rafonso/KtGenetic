package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.ChromosomeFitnessComparator

internal class TruncateFitnessSelectionStrategy<C : Chromosome<*>>(override val generationSize: Int) :
        SelectionStrategy<C> {

    override fun select(children: List<C>): List<C> = children.
            sortedWith(ChromosomeFitnessComparator()).
            reversed().
            subList(0, generationSize)

}