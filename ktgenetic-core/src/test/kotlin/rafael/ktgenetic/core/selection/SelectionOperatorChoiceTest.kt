package rafael.ktgenetic.core.selection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.Environment

class SelectionOperatorChoiceTest {

    @Mock
    lateinit var environment: Environment<Char, Chromosome<Char>>

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(environment.generationSize).thenReturn(10)
    }

    @ParameterizedTest
    @CsvSource(
        "TRUNCATE, rafael.ktgenetic.core.selection.TruncateSelectionOperator",
        "ROULETTE, rafael.ktgenetic.core.selection.RouletteSelectionOperator",
        "TOURNAMENT, rafael.ktgenetic.core.selection.TournamentSelectionOperator",
        "LINEAR_RANKING, rafael.ktgenetic.core.selection.LinearRankingSelectionOperator",
        "EXPONENTIAL_RANKING, rafael.ktgenetic.core.selection.ExponentialRankingSelectionOperator"
    )
    fun `SelectionOperatorChoice returns its respective Operator when elitism is false`(
        choice: SelectionOperatorChoice,
        operatorClass: Class<*>
    ) {
        val operator = choice.chooseSelectionOperator(environment, elitism = false, allowRepetition = false)

        assertTrue(operatorClass.isInstance(operator))
        assertFalse(operator is ElitismDelegateSelectionOperator)
    }

    @ParameterizedTest
    @CsvSource(
        "TRUNCATE, rafael.ktgenetic.core.selection.TruncateSelectionOperator",
        "ROULETTE, rafael.ktgenetic.core.selection.RouletteSelectionOperator",
        "TOURNAMENT, rafael.ktgenetic.core.selection.TournamentSelectionOperator",
        "LINEAR_RANKING, rafael.ktgenetic.core.selection.LinearRankingSelectionOperator",
        "EXPONENTIAL_RANKING, rafael.ktgenetic.core.selection.ExponentialRankingSelectionOperator"
    )
    fun `SelectionOperatorChoice returns its a ElitismDelegateSelectionOperator when elitism is true`(
        choice: SelectionOperatorChoice,
        operatorClass: Class<*>
    ) {
        val operator = choice.chooseSelectionOperator(environment, elitism = true, allowRepetition = false)

        assertFalse(operatorClass.isInstance(operator))
        assertTrue(operator is ElitismDelegateSelectionOperator)
    }

    @Test
    fun `codeToSelectionOperatorChoice returns the respective SelectionOperatorChoice`() {
        SelectionOperatorChoice.entries.forEach {
            val code = it.code
            val choice = codeToSelectionOperatorChoice(code)

            assertTrue(it == choice)
        }
    }


    @Test
    fun `codeToSelectionOperatorChoice throws IllegalArgumentException when the code is invalid`() {
        assertThrows(IllegalArgumentException::class.java) {
            codeToSelectionOperatorChoice("invalid")
        }
    }
}