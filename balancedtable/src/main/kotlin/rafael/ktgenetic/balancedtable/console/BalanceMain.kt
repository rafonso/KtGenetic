package rafael.ktgenetic.balancedtable.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.Environment
import rafael.ktgenetic.balancedtable.Balance
import rafael.ktgenetic.balancedtable.BalanceEnvironment
import rafael.ktgenetic.balancedtable.Box
import rafael.ktgenetic.console.*
import rafael.ktgenetic.processor.GeneticProcessorChoice

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

    return BalanceEnvironment(
            weights, //
            line.getMaxGenerations(),
            line.getPopulationByGeneration())
}

private fun showEnvironmentDetails(environment: Environment<Box, Balance>): String {
    val esEnvironment = environment as BalanceEnvironment

    return "Quantity Of Boxes: ${esEnvironment.originalBoxes.size}, Original Boxes: ${esEnvironment.originalBoxes}"
}

fun main(args: Array<String>) = executeMain(
        args,
        ::addOptions,
        ::validateParameters,
        ::getEnvironment,
        GeneticProcessorChoice.ORDERED,
        ::showEnvironmentDetails
)

