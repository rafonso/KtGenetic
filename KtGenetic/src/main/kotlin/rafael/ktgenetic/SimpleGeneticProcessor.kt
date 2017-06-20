package rafael.ktgenetic

import rafael.ktgenetic.selection.SelectionStrategy

internal class SimpleGeneticProcessor<G, C : Chromosome<G>>(environment: Environment<G, C>,
                                                   selectionStrategy: SelectionStrategy<C>) :
        GeneticProcessor<G, C>(environment, selectionStrategy) {

    override fun <G> executeCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): Pair<List<G>, List<G>> =
            super.basicCrossing(pieces1, pieces2)

}