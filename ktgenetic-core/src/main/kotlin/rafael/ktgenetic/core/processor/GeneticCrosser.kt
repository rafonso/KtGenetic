package rafael.ktgenetic.core.processor

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.ListPieces

/**
 * This sealed class represents a Genetic Crosser used in genetic algorithms.
 * Genetic Crosser is responsible for crossing two parent chromosomes to generate offspring.
 *
 * @param <G> The type of the Gene
 * @param <C> The type of the Chromosome
 */
internal sealed class GeneticCrosser<G, C : Chromosome<G>> {

    /**
     * Executes the crossing of two parent chromosomes.
     * @param pieces1 The pieces of the first parent chromosome
     * @param pieces2 The pieces of the second parent chromosome
     * @param environment The environment of the genetic algorithm
     * @return The list of offspring chromosomes
     */
    abstract fun executeCrossing(
        pieces1: ListPieces<G>,
        pieces2: ListPieces<G>,
        environment: Environment<G, C>
    ): List<C>

}

/**
 * This class represents a Simple Genetic Crosser used in genetic algorithms.
 * Simple Genetic Crosser crosses two parent chromosomes by simply swapping their genes.
 *
 * @param <G> The type of the Gene
 * @param <C> The type of the Chromosome
 *
 * @see GeneticCrosser
 */
internal class SimpleGeneticCrosser<G, C : Chromosome<G>> : GeneticCrosser<G, C>() {

    /**
     * Executes the crossing of two parent chromosomes by simply swapping their genes.
     * @param pieces1 The pieces of the first parent chromosome
     * @param pieces2 The pieces of the second parent chromosome
     * @param environment The environment of the genetic algorithm
     * @return The list of offspring chromosomes
     */
    override fun executeCrossing(
        pieces1: ListPieces<G>,
        pieces2: ListPieces<G>,
        environment: Environment<G, C>
    ): List<C> = listOf(
        environment.createNewChromosome(pieces2.left + pieces1.core + pieces2.right),
        environment.createNewChromosome(pieces1.left + pieces2.core + pieces1.right)
    )

}

/**
 * This class represents an Ordered Genetic Crosser used in genetic algorithms.
 * Ordered Genetic Crosser crosses two parent chromosomes by preserving the relative order of their genes.
 *
 * @param <G> The type of the Gene
 * @param <C> The type of the Chromosome
 *
 * @see GeneticCrosser
 */
internal class OrderedGeneticCrosser<G, C : Chromosome<G>> : GeneticCrosser<G, C>() {

    private val basicCrosser = SimpleGeneticCrosser<G, C>()

    private fun cross(core: List<G>, parent: List<G>, firstCutPoint: Int, environment: Environment<G, C>): C {
        val diff = parent - core.toSet()
        val part1 = diff.subList(0, firstCutPoint)
        val part2 = diff.subList(firstCutPoint, diff.size)

        return environment.createNewChromosome(part1 + core + part2)
    }

    private fun executeCross(pieces1: ListPieces<G>, pieces2: ListPieces<G>, environment: Environment<G, C>): List<C> {
        val child1 = cross(pieces1.core, pieces2.join, pieces2.left.size, environment)
        val child2 = cross(pieces2.core, pieces1.join, pieces1.left.size, environment)

        return listOf(child1, child2)
    }

    /**
     * Executes the crossing of two parent chromosomes by preserving the relative order of their genes.
     * If there is an intersection between the genes of the parents, it uses the executeCross method.
     * Otherwise, it uses the basicCrosser's executeCrossing method.
     * @param pieces1 The pieces of the first parent chromosome
     * @param pieces2 The pieces of the second parent chromosome
     * @param environment The environment of the genetic algorithm
     * @return The list of offspring chromosomes
     */
    override fun executeCrossing(
        pieces1: ListPieces<G>,
        pieces2: ListPieces<G>,
        environment: Environment<G, C>
    ): List<C> {
        val thereIsIntersection =
            pieces1.core.intersect((pieces2.left + pieces2.right).toSet()).isNotEmpty() ||
                    pieces2.core.intersect((pieces1.left + pieces1.right).toSet()).isNotEmpty()

        return if (thereIsIntersection) executeCross(pieces1, pieces2, environment)
        else basicCrosser.executeCrossing(pieces1, pieces2, environment)
    }

}
