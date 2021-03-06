package rafael.ktgenetic.equalstring.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.console.*
import rafael.ktgenetic.equalstring.*
import rafael.ktgenetic.core.processor.GeneticCrossingType

private const val WORD_PARAMETER = "w"
private const val FITNESS_PARAMETER = "f"

private val EQUAL_CHARS_FITNESS_PARAMETER = StringFitnessChoice.EQUAL_CHARS.code
private val SUBTRACT_CHARS_FITNESS_PARAMETER = StringFitnessChoice.SUBSTRACT_CHARS.code

private fun addOptions(options: Options) {
    options.addOption(WORD_PARAMETER, true, "Expression Target, rounded by quotation marks")
    options.addOption(
        FITNESS_PARAMETER, true, "Fitness function. Values: " +
                "$EQUAL_CHARS_FITNESS_PARAMETER - Equal Chars, " +
                "$SUBTRACT_CHARS_FITNESS_PARAMETER - Subtract Chars " +
                "(Default Value: $EQUAL_CHARS_FITNESS_PARAMETER)"
    )
}

private fun validateParameters(line: CommandLine) {
    require(line.hasOption(WORD_PARAMETER)) { "Please provide the word to be processed" }
    require(
        !(line.hasOption(FITNESS_PARAMETER) &&
                !setOf(EQUAL_CHARS_FITNESS_PARAMETER, SUBTRACT_CHARS_FITNESS_PARAMETER).contains(
                    line.getOptionValue(
                        FITNESS_PARAMETER
                    )
                ))
    ) {
        "Fitness parameter should be $EQUAL_CHARS_FITNESS_PARAMETER " +
                "or $SUBTRACT_CHARS_FITNESS_PARAMETER"
    }
}

private fun getFitnessFunction(line: CommandLine): StringFitness =
    when (line.getOptionValue(FITNESS_PARAMETER, EQUAL_CHARS_FITNESS_PARAMETER)) {
        EQUAL_CHARS_FITNESS_PARAMETER -> EqualCharsFitness()
        SUBTRACT_CHARS_FITNESS_PARAMETER -> SubtractCharsFitness()
        else -> throw IllegalArgumentException(
            "Fitness parameter should be $EQUAL_CHARS_FITNESS_PARAMETER " +
                    "or $SUBTRACT_CHARS_FITNESS_PARAMETER"
        )
    }

private fun getEnvironment(line: CommandLine) =
    EqualStringEnvironment(
        line.getOptionValue(WORD_PARAMETER), //
        getFitnessFunction(line),
        line.getMaxGenerations(),
        line.getPopulationByGeneration()
    )

private fun showEnvironmentDetails(environment: Environment<Char, Word>): String {
    val esEnvironment = environment as EqualStringEnvironment

    return "Target: '${esEnvironment.target}', fitnessFunction: ${esEnvironment.fitnessFunction.javaClass.simpleName}"
}

fun main(args: Array<String>) = executeMain(
    args,
    ::addOptions,
    ::validateParameters,
    ::getEnvironment,
    GeneticCrossingType.SIMPLE,
    ::showEnvironmentDetails
)
