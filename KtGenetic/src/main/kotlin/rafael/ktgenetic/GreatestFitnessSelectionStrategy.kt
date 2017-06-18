package rafael.ktgenetic

class GreatestFitnessSelectionStrategy<C : Chromosome<*>>(override val generationSize: Int) : SelectionStrategy<C> {

    override fun select(children: List<C>): List<C> {
        return children.sortedWith(ChromosomeFitnessComparator()).reversed().subList(0, generationSize)
    }

}