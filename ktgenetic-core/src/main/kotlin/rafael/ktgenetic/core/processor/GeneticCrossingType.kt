package rafael.ktgenetic.core.processor

import rafael.ktgenetic.core.Chromosome

/**
 * Enum representing the different types of genetic crossing methods available in the genetic algorithm.
 * Each enum value is associated with a specific genetic crosser class.
 *
 * See https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm) for more information.
 */
enum class GeneticCrossingType {

    /**
     * Represents the Simple Genetic Crosser.
     * Simple Genetic Crosser crosses two parent chromosomes by simply swapping their genes.
     */
    SIMPLE {

        /**
         * Returns a Simple Genetic Crosser.
         * @param <G> The type of the Gene
         * @param <C> The type of the Chromosome
         * @return The Simple Genetic Crosser
         */
        override fun <G, C : Chromosome<G>> getGeneticCrosser(): GeneticCrosser<G, C> = SimpleGeneticCrosser()

    },
    /**
     * Represents the Ordered Genetic Crosser.
     * Ordered Genetic Crosser crosses two parent chromosomes by preserving the relative order of their genes.
     */
    ORDERED {

        /**
         * Returns an Ordered Genetic Crosser.
         * @param <G> The type of the Gene
         * @param <C> The type of the Chromosome
         * @return The Ordered Genetic Crosser
         */
        override fun <G, C : Chromosome<G>> getGeneticCrosser(): GeneticCrosser<G, C> = OrderedGeneticCrosser()

    }
    ;

    /**
     * Abstract function to get a genetic crosser based on the GeneticCrossingType.
     * @param <G> The type of the Gene
     * @param <C> The type of the Chromosome
     * @return The genetic crosser
     */
    internal abstract fun <G, C : Chromosome<G>> getGeneticCrosser(): GeneticCrosser<G, C>

}
