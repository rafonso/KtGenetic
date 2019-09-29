package rafael.ktgenetic.processor

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ListPieces
import rafael.ktgenetic.selection.SelectionOperator

internal class SimpleGeneticProcessor<G, C : Chromosome<G>>(environment: Environment<G, C>,
                                                            selectionOperator: SelectionOperator<C>) :
        GeneticProcessor<G, C>(environment, selectionOperator) {

    override fun executeCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): List<C> =
            super.basicCrossing(pieces1, pieces2)

}