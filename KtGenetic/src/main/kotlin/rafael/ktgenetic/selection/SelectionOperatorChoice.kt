package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class SelectionOperatorChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(environment: Environment<*, C>): SelectionOperator<C> =
                TruncateSelectionOperator<C>(environment.generationSize)
    },
    ROULETTE_ELITISM("r", "Roulette with Elitism") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(environment: Environment<*, C>): SelectionOperator<C> =
                ElitismDelegateSelectionOperator<C>(
                        RouletteSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                        environment.generationSize
                )
    };

    internal abstract fun <C : Chromosome<*>> chooseSelectionOperator(environment: Environment<*, C>): SelectionOperator<C>
}

fun codeToSelectionOperatorChoice(code: String): SelectionOperatorChoice {
    SelectionOperatorChoice.values()
            .filter { code == it.code }
            .forEach { return it }

    throw IllegalArgumentException("Illegal Selection Operator Choice code: $code")
}