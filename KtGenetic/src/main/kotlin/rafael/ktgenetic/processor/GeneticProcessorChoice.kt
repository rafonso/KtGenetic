package rafael.ktgenetic.processor

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import rafael.ktgenetic.selection.SelectionOperator

enum class GeneticProcessorChoice {

    SIMPLE {
        override fun <G, C : Chromosome<G>> newInstance(environment: Environment<G, C>,
                                                        selectionOperator: SelectionOperator<C>):
                GeneticProcessor<G, C> =
                SimpleGeneticProcessor(environment, selectionOperator)
    },
    ORDERED {
        override fun <G, C : Chromosome<G>> newInstance(environment: Environment<G, C>,
                                                        selectionOperator: SelectionOperator<C>):
                GeneticProcessor<G, C> =
                OrderedGeneticProcessor(environment, selectionOperator)
    };

    internal abstract fun <G, C : Chromosome<G>> newInstance(environment: Environment<G, C>,
                                                             selectionOperator: SelectionOperator<C>):
            GeneticProcessor<G, C>
}