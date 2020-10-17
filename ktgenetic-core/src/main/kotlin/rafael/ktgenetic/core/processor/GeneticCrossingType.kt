package rafael.ktgenetic.core.processor

import rafael.ktgenetic.core.Chromosome

/**
 * Indicates the type of crossing to be used.
 *
 * @see (Wikipedia)[https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)]
 */
enum class GeneticCrossingType {

    /**
     * 2-point crossover
     */
    SIMPLE {

        override fun <G, C : Chromosome<G>> getGeneticCrosser(): GeneticCrosser<G, C> = SimpleGeneticCrosser()

    },
    /**
     * 2-point crossover for ordered list
     */
    ORDERED {

        override fun <G, C : Chromosome<G>> getGeneticCrosser(): GeneticCrosser<G, C> = OrderedGeneticCrosser()

    }
    ;

    internal abstract fun <G, C : Chromosome<G>> getGeneticCrosser(): GeneticCrosser<G, C>

}
