package rafael.ktgenetic.processor

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import rafael.ktgenetic.selection.SelectionStrategy

enum class GeneticProcessorChoice {

    SIMPLE {
        override fun <G, C : Chromosome<G>> newInstance(environment: Environment<G, C>,
                                                        selectionStrategy: SelectionStrategy<C>):
                GeneticProcessor<G, C> =
                SimpleGeneticProcessor(environment, selectionStrategy)
    },
    ORDERED {
        override fun <G, C : Chromosome<G>> newInstance(environment: Environment<G, C>,
                                                        selectionStrategy: SelectionStrategy<C>):
                GeneticProcessor<G, C> =
                OrderedGeneticProcessor(environment, selectionStrategy)
    };

    internal abstract fun <G, C : Chromosome<G>> newInstance(environment: Environment<G, C>,
                                                             selectionStrategy: SelectionStrategy<C>):
            GeneticProcessor<G, C>
}