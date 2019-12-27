package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class SelectionOperatorChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            elitism: Boolean,
            allowRepetition: Boolean
        ): SelectionOperator<C> = if (elitism)
            ElitismDelegateSelectionOperator(
                TruncateSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                environment.generationSize,
                allowRepetition
            )
        else TruncateSelectionOperator(environment.generationSize, allowRepetition)
    },
    ROULETTE("r", "Roulette") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            elitism: Boolean,
            allowRepetition: Boolean
        ): SelectionOperator<C> = if (elitism)
            ElitismDelegateSelectionOperator(
                RouletteSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                environment.generationSize, allowRepetition
            )
        else RouletteSelectionOperator(environment.generationSize, allowRepetition)
    },
    TOURNAMENT("o", "Tournament") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            elitism: Boolean,
            allowRepetition: Boolean
        ): SelectionOperator<C> = if (elitism)
            ElitismDelegateSelectionOperator(
                TournamentSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                environment.generationSize, allowRepetition
            )
        else TournamentSelectionOperator(environment.generationSize, allowRepetition)
    },
    LINEAR_RANKING("l", "Linear Ranking") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            elitism: Boolean,
            allowRepetition: Boolean
        ): SelectionOperator<C> = if (elitism)
            ElitismDelegateSelectionOperator(
                LinearRankingSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                environment.generationSize, allowRepetition
            )
        else LinearRankingSelectionOperator(environment.generationSize, allowRepetition)
    },
    EXPONENTIAL_RANKING("x", "Exponential Ranking") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            elitism: Boolean,
            allowRepetition: Boolean
        ): SelectionOperator<C> = if (elitism)
            ElitismDelegateSelectionOperator(
                ExponentialRankingSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                environment.generationSize, allowRepetition
            )
        else ExponentialRankingSelectionOperator(environment.generationSize, allowRepetition)
    }
    ;

    // "internal" removed
    abstract fun <C : Chromosome<*>> chooseSelectionOperator(
        environment: Environment<*, C>,
        elitism: Boolean,
        allowRepetition: Boolean
    ): SelectionOperator<C>
}

fun codeToSelectionOperatorChoice(code: String): SelectionOperatorChoice {
    SelectionOperatorChoice.values()
        .filter { code == it.code }
        .forEach { return it }

    throw IllegalArgumentException("Illegal Selection Operator Choice code: $code")
}
