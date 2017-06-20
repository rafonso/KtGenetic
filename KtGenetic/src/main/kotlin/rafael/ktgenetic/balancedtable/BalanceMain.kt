package rafael.ktgenetic.balancedtable

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.GeneticProcessorChoice
import rafael.ktgenetic.console.CHILDREN_TO_SURVIVE_PARAMETER
import rafael.ktgenetic.console.GENERATIONS_PARAMETER
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.mainLogger

private const val WEIGHT_PARAMETER = "w"

private fun addOptions(options: Options) {
    options.addOption(WEIGHT_PARAMETER, true, "Weights, rounded by quotation marks and separated by spaces")
}

private fun validateParameters(line: CommandLine) {
    if (!line.hasOption(WEIGHT_PARAMETER)) {
        throw IllegalArgumentException("Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
    }
    if (!line.getOptionValue(WEIGHT_PARAMETER).matches(Regex("^((\\d+) *)+$"))) {
        throw IllegalArgumentException("Weights incorrect: \"${line.getOptionValue(WEIGHT_PARAMETER)}\"; " +
                "Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
    }
}

private fun getEnvironment(line: CommandLine): BalanceEnvironment {
    val weights = line.getOptionValue(WEIGHT_PARAMETER).trim().split(Regex(" +")).map { Integer.parseInt(it) }
    mainLogger.info("Blocks: $weights")
    return BalanceEnvironment(
            weights, //
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "1000000").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt())
}

fun main(args: Array<String>) {
    executeMain(
            args,
            ::addOptions,
            ::validateParameters,
            ::getEnvironment,
            GeneticProcessorChoice.ORDERED)
}

