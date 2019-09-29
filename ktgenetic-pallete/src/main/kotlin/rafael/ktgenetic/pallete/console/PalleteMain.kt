package rafael.ktgenetic.pallete.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.Environment
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getIntOptionValue
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.pallete.*
import rafael.ktgenetic.processor.GeneticProcessorChoice

private const val WEIGHT_PARAMETER = "w"
private const val ROWS_PARAMETER = "rows"
private const val COLS_PARAMETER = "cols"

private fun addOptions(options: Options) {
    options.addOption(WEIGHT_PARAMETER, true, "Weights, rounded by quotation marks and separated by spaces")
    options.addOption(ROWS_PARAMETER, true, "Number of rows in the pallete")
    options.addOption(COLS_PARAMETER, true, "Number of columns in the pallete")
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

private fun getEnvironment(line: CommandLine): PalleteEnvironment {
    val (normalizedPallet, palleteDimensions) = getPallete(
            line.getOptionValue(WEIGHT_PARAMETER).trim().split(Regex(" +")).map { Integer.parseInt(it) },
            line.getIntOptionValue(ROWS_PARAMETER, 0),
            line.getIntOptionValue(COLS_PARAMETER, 0))

    return PalleteEnvironment(normalizedPallet, palleteDimensions, //
            line.getMaxGenerations(),
            line.getPopulationByGeneration())

}

private fun showEnvironmentDetails(environment: Environment<Box, Pallete>): String {
    val esEnvironment = environment as PalleteEnvironment

    return "Dimension: ${esEnvironment.palleteDimension}, Original Boxes: ${esEnvironment.originalBoxes}"
}

fun main(args: Array<String>) = executeMain(
        args,
        ::addOptions,
        ::validateParameters,
        ::getEnvironment,
        GeneticProcessorChoice.ORDERED,
        ::showEnvironmentDetails,
        { p, e -> p.addListener(e as PalleteEnvironment) }
)

