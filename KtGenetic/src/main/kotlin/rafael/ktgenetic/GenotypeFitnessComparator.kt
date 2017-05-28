package rafael.ktgenetic

class GenotypeFitnessComparator<G>: Comparator<Genotype<G>> {
    override fun compare(g1: Genotype<G>, g2: Genotype<G>): Int = g1.fitness.compareTo(g2.fitness)
}