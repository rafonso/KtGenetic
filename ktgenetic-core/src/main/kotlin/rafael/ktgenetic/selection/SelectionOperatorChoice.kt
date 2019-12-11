package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class SelectionOperatorChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            allowRepetition: Boolean
        ): SelectionOperator<C> =
                TruncateSelectionOperator(environment.generationSize, allowRepetition)
    },
    ROULETTE_ELITISM("r", "Roulette with Elitism") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            allowRepetition: Boolean
        ): SelectionOperator<C> =
                ElitismDelegateSelectionOperator(
                    RouletteSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                    environment.generationSize, allowRepetition
                )
    },
    TOURNAMENT_ELITISM("o", "Tournament with Elitism") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            allowRepetition: Boolean
        ): SelectionOperator<C> =
                ElitismDelegateSelectionOperator(
                    TournamentSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                    environment.generationSize
                )
    },
    LINEAR_RANKING_ELITISM("l", "Linear Ranking with Elitism") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            allowRepetition: Boolean
        ): SelectionOperator<C> =
                ElitismDelegateSelectionOperator(
                    LinearRankingSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                    environment.generationSize
                )
    },
    EXPONENTIAL_RANKING_ELITISM("x", "Exponential Ranking with Elitism") {
        override fun <C : Chromosome<*>> chooseSelectionOperator(
            environment: Environment<*, C>,
            allowRepetition: Boolean
        ): SelectionOperator<C> =
                ElitismDelegateSelectionOperator(
                    ExponentialRankingSelectionOperator::class.primaryConstructor!! as KFunction<*>,
                    environment.generationSize
                )
    }
    ;

    // "internal" removed
    abstract fun <C : Chromosome<*>> chooseSelectionOperator(
        environment: Environment<*, C>,
        allowRepetition: Boolean
    ): SelectionOperator<C>
}

fun codeToSelectionOperatorChoice(code: String): SelectionOperatorChoice {
    SelectionOperatorChoice.values()
        .filter { code == it.code }
        .forEach { return it }

    throw IllegalArgumentException("Illegal Selection Operator Choice code: $code")
}