package rafael.ktgenetic.equalstring

import rafael.ktgenetic.GeneticProcessorChoice

private const val WORD_PARAMETER = "w"
private const val FITNESS_PARAMETER = "f"

private const val EQUAL_CHARS_FITNESS_PARAMETER = "e"
private const val SUBTRACT_CHARS_FITNESS_PARAMETER = "s"

private fun addOptions(options: org.apache.commons.cli.Options) {
    options.addOption(rafael.ktgenetic.equalstring.WORD_PARAMETER, true, "Expression Target, rounded by quotation marks")
    options.addOption(rafael.ktgenetic.equalstring.FITNESS_PARAMETER, true, "Fitness function. Values: " +
            "${rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER} - Equal Chars, " +
            "${rafael.ktgenetic.equalstring.SUBTRACT_CHARS_FITNESS_PARAMETER} - Subtract Chars " +
            "(Default Value: ${rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER})")
}

private fun validateParameters(line: org.apache.commons.cli.CommandLine) {
    if (!line.hasOption(rafael.ktgenetic.equalstring.WORD_PARAMETER)) {
        throw IllegalArgumentException("Please provide the word to be processed")
    }
    if (line.hasOption(rafael.ktgenetic.equalstring.FITNESS_PARAMETER) &&
            !setOf(rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER, rafael.ktgenetic.equalstring.SUBTRACT_CHARS_FITNESS_PARAMETER).contains(line.getOptionValue(rafael.ktgenetic.equalstring.FITNESS_PARAMETER))) {
        throw IllegalArgumentException("Fitness parameter should be ${rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER} " +
                "or ${rafael.ktgenetic.equalstring.SUBTRACT_CHARS_FITNESS_PARAMETER}")
    }
}

private fun getFitnessFunction(line: org.apache.commons.cli.CommandLine): rafael.ktgenetic.equalstring.StringFitness {
    val fitnessParameter = line.getOptionValue(rafael.ktgenetic.equalstring.FITNESS_PARAMETER, rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER)

    val fitnessFunction: rafael.ktgenetic.equalstring.StringFitness
    when (fitnessParameter) {
        rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER -> fitnessFunction = rafael.ktgenetic.equalstring.EqualCharsFitness()
        rafael.ktgenetic.equalstring.SUBTRACT_CHARS_FITNESS_PARAMETER -> fitnessFunction = rafael.ktgenetic.equalstring.SubtractCharsFitness()
        else -> throw IllegalArgumentException("Fitness parameter should be ${rafael.ktgenetic.equalstring.EQUAL_CHARS_FITNESS_PARAMETER} " +
                "or ${rafael.ktgenetic.equalstring.SUBTRACT_CHARS_FITNESS_PARAMETER}")
    }

    return fitnessFunction
}

private fun getEnvironment(line: org.apache.commons.cli.CommandLine) =
        rafael.ktgenetic.equalstring.EqualStringEnvironment(
                line.getOptionValue(WORD_PARAMETER), //
                getFitnessFunction(line),
                line.getOptionValue(rafael.ktgenetic.console.GENERATIONS_PARAMETER, "100").toInt(),
                line.getOptionValue(rafael.ktgenetic.console.CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt())

fun main(args: Array<String>) {
    rafael.ktgenetic.console.executeMain(
            args,
            ::addOptions,
            ::validateParameters,
            ::getEnvironment,
            GeneticProcessorChoice.SIMPLE
    )
}
