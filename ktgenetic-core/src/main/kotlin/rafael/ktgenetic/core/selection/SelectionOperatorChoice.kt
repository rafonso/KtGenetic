package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.Environment
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor


/**
 * Enum representing the different types of selection operators available in the genetic algorithm.
 * Each enum value is associated with a specific selection operator class.
 *
 * @property code The code representing the selection operator.
 * @property description The description of the selection operator.
 */
enum class SelectionOperatorChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation") {
        /**
         * Chooses the Truncate Selection Operator.
         * If elitism is enabled, it uses the ElitismDelegateSelectionOperator.
         */
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
        /**
         * Chooses the Roulette Selection Operator.
         * If elitism is enabled, it uses the ElitismDelegateSelectionOperator.
         */
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
        /**
         * Chooses the Tournament Selection Operator.
         * If elitism is enabled, it uses the ElitismDelegateSelectionOperator.
         */
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
        /**
         * Chooses the Linear Ranking Selection Operator.
         * If elitism is enabled, it uses the ElitismDelegateSelectionOperator.
         */
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
        /**
         * Chooses the Exponential Ranking Selection Operator.
         * If elitism is enabled, it uses the ElitismDelegateSelectionOperator.
         */
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

    /**
     * Abstract function to choose a selection operator based on the environment, elitism, and allowRepetition parameters.
     * @param environment The environment of the genetic algorithm
     * @param elitism If elitism is enabled
     * @param allowRepetition If repetition of chromosomes is allowed
     * @return The chosen selection operator
     */
    abstract fun <C : Chromosome<*>> chooseSelectionOperator(
        environment: Environment<*, C>,
        elitism: Boolean,
        allowRepetition: Boolean
    ): SelectionOperator<C>

}

/**
 * Function to convert a code to a SelectionOperatorChoice.
 * @param code The code to convert
 * @return The corresponding SelectionOperatorChoice
 * @throws IllegalArgumentException If the code does not correspond to any SelectionOperatorChoice
 */
fun codeToSelectionOperatorChoice(code: String): SelectionOperatorChoice {
    SelectionOperatorChoice.entries
        .filter { code == it.code }
        .forEach { return it }

    throw IllegalArgumentException("Illegal Selection Operator Choice code: $code")
}
