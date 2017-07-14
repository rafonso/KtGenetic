package rafael.ktgenetic.equalstring.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.Environment
import rafael.ktgenetic.console.*
import rafael.ktgenetic.equalstring.*
import rafael.ktgenetic.processor.GeneticProcessorChoice

private const val WORD_PARAMETER = "w"
private const val FITNESS_PARAMETER = "f"

private const val EQUAL_CHARS_FITNESS_PARAMETER = "e"
private const val SUBTRACT_CHARS_FITNESS_PARAMETER = "s"

private fun addOptions(options: Options) {
    options.addOption(WORD_PARAMETER, true, "Expression Target, rounded by quotation marks")
    options.addOption(FITNESS_PARAMETER, true, "Fitness function. Values: " +
            "${EQUAL_CHARS_FITNESS_PARAMETER} - Equal Chars, " +
            "${SUBTRACT_CHARS_FITNESS_PARAMETER} - Subtract Chars " +
            "(Default Value: ${EQUAL_CHARS_FITNESS_PARAMETER})")
}

private fun validateParameters(line: CommandLine) {
    if (!line.hasOption(WORD_PARAMETER)) {
        throw IllegalArgumentException("Please provide the word to be processed")
    }
    if (line.hasOption(FITNESS_PARAMETER) &&
            !setOf(EQUAL_CHARS_FITNESS_PARAMETER, SUBTRACT_CHARS_FITNESS_PARAMETER).contains(line.getOptionValue(FITNESS_PARAMETER))) {
        throw IllegalArgumentException("Fitness parameter should be ${EQUAL_CHARS_FITNESS_PARAMETER} " +
                "or ${SUBTRACT_CHARS_FITNESS_PARAMETER}")
    }
}

private fun getFitnessFunction(line: CommandLine): StringFitness {
    val fitnessParameter = line.getOptionValue(FITNESS_PARAMETER, EQUAL_CHARS_FITNESS_PARAMETER)

    val fitnessFunction: StringFitness
    when (fitnessParameter) {
        EQUAL_CHARS_FITNESS_PARAMETER -> fitnessFunction = EqualCharsFitness()
        SUBTRACT_CHARS_FITNESS_PARAMETER -> fitnessFunction = SubtractCharsFitness()
        else -> throw IllegalArgumentException("Fitness parameter should be ${EQUAL_CHARS_FITNESS_PARAMETER} " +
                "or ${SUBTRACT_CHARS_FITNESS_PARAMETER}")
    }

    return fitnessFunction
}

private fun getEnvironment(line: CommandLine) =
        EqualStringEnvironment(
                line.getOptionValue(WORD_PARAMETER), //
                getFitnessFunction(line),
                line.getMaxGenerations(),
                line.getPopulationByGeneration())

private fun showEnvironmentDetails(environment: Environment<Char, Word>): String {
    val esEnvironment = environment as EqualStringEnvironment

    return "Target: '${esEnvironment.target}', fitnessFunction: ${esEnvironment.fitnessFunction.javaClass.simpleName}"
}

fun main(args: Array<String>) = executeMain(
        args,
        ::addOptions,
        ::validateParameters,
        ::getEnvironment,
        GeneticProcessorChoice.SIMPLE,
        ::showEnvironmentDetails
)
