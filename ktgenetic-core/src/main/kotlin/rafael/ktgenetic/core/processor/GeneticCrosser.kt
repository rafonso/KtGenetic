package rafael.ktgenetic.core.processor

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.ListPieces

internal sealed class GeneticCrosser<G, C : Chromosome<G>> {

    abstract fun executeCrossing(
        pieces1: ListPieces<G>,
        pieces2: ListPieces<G>,
        environment: Environment<G, C>
    ): List<C>

}

internal class SimpleGeneticCrosser<G, C : Chromosome<G>> : GeneticCrosser<G, C>() {

    override fun executeCrossing(
        pieces1: ListPieces<G>,
        pieces2: ListPieces<G>,
        environment: Environment<G, C>
    ): List<C> = listOf(
        environment.createNewChromosome(pieces2.left + pieces1.core + pieces2.right),
        environment.createNewChromosome(pieces1.left + pieces2.core + pieces1.right)
    )

}

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
