package rafael.ktgenetic

/**
 * Compares 2 [Genotype]'s according their [Genotype.fitness]
 */
class GenotypeFitnessComparator<G, N :  Genotype<G>>: Comparator<N> {
    override fun compare(g1: N, g2: N): Int = g1.fitness.compareTo(g2.fitness)
}