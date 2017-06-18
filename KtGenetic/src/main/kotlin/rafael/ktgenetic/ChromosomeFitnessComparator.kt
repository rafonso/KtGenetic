package rafael.ktgenetic

/**
 * Compares 2 [Chromosome]'s according their [Chromosome.fitness]
 */
class ChromosomeFitnessComparator<C : Chromosome<*>> : Comparator<C> {
    override fun compare(c1: C, c2: C): Int = c1.fitness.compareTo(c2.fitness)
}