package rafael.ktgenetic.pallete.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.Environment
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getIntOptionValue
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.pallete.Box
import rafael.ktgenetic.pallete.Pallete
import rafael.ktgenetic.pallete.PalleteDimensions
import rafael.ktgenetic.pallete.PalleteEnvironment
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

fun getPallete(weights: Collection<Int>, rows: Int, cols: Int): Pair<List<Int>, PalleteDimensions> {

    fun getPalleteDimensions(): Pair<Int, Int> {
        if (rows == 0 && cols == 0) {
            val sqrtSize = Math.sqrt(weights.size.toDouble()).toInt()
            val side: Int = sqrtSize + (if (weights.size == sqrtSize * sqrtSize) 0 else 1)
            return Pair(side, side)
        }
        if (rows == 0 && cols != 0) {
            return Pair((weights.size / cols) + (if (weights.size % cols == 0) 0 else 1), cols)
        }
        if (rows != 0 && cols == 0) {
            return Pair(rows, (weights.size / rows) + (if (weights.size % rows == 0) 0 else 1))
        }

        if ((rows * cols) < weights.size) {
            throw IllegalArgumentException("Number of Rows ($rows) times the number of columns ($cols) " +
                    "lesser than the size of weights (${weights.size})")
        }

        return Pair(rows, cols)
    }

    fun getNormalizedPallete(r: Int, c: Int): List<Int> {
        if (weights.size == r * c) {
            return weights.toList()
        }

        val result = IntArray(c * r)
        weights.forEachIndexed { index, w -> result[index] = w }
        return result.toList()
    }

    val (r, c) = getPalleteDimensions()
    val normalizedPallet = getNormalizedPallete(r, c)

    return Pair(normalizedPallet, PalleteDimensions(r, c))
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

