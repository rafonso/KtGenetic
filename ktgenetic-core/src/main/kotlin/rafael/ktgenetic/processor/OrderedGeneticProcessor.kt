package rafael.ktgenetic.processor

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ListPieces
import rafael.ktgenetic.selection.SelectionOperator

internal class OrderedGeneticProcessor<G, C : Chromosome<G>>(environment: Environment<G, C>,
                                                             selectionOperator: SelectionOperator<C>) :
        GeneticProcessor<G, C>(environment, selectionOperator) {

    private fun executeCross(pieces1: ListPieces<G>, pieces2: ListPieces<G>): List<C> {

        fun cross(core: List<G>, parent: List<G>, firstCutPoint: Int): C {
            val diff = parent - core
            val part1 = diff.subList(0, firstCutPoint)
            val part2 = diff.subList(firstCutPoint, diff.size)
            return environment.createNewChromosome(part1 + core + part2)
        }

        val child1 = cross(pieces1.core, pieces2.join, pieces2.left.size)
        val child2 = cross(pieces2.core, pieces1.join, pieces1.left.size)

        return listOf(child1, child2)
    }

    override fun executeCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): List<C> {
        if (!pieces1.core.intersect(pieces2.left + pieces2.right).isEmpty() || !pieces2.core.intersect(pieces1.left + pieces1.right).isEmpty()) {
            return executeCross(pieces1, pieces2)
        }
        return super.basicCrossing(pieces1, pieces2)
    }

}