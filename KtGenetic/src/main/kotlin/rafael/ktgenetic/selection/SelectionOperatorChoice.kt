package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment

enum class SelectionOperatorChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(environment: Environment<*, C>): SelectionOperator<C> =
                TruncateSelectionOperator<C>(environment.generationSize)
    },
    ROULETTE_ELITISM("r", "Roulette with Elitism") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(environment: Environment<*, C>): SelectionOperator<C> =
                RouletteElitismSelectionOperator<C>(environment.generationSize)
    };

    internal abstract fun <C : Chromosome<*>> chooseSelectionOperator(environment: Environment<*, C>): SelectionOperator<C>
}

fun codeToSelectionOperatorChoice(code: String): SelectionOperatorChoice {
    for (ch in SelectionOperatorChoice.values()) {
        if (code == ch.code) {
            return ch
        }
    }

    throw IllegalArgumentException("Illegal Selection Operator Choice code: $code")
}