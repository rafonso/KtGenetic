package rafael.ktgenetic.pallete

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.CHILDREN_TO_SURVIVE_PARAMETER
import rafael.ktgenetic.GENERATIONS_PARAMETER
import rafael.ktgenetic.executeMain

private const val WEIGHT_PARAMETER = "w"
private const val ROWS_PARAMETER = "rows"
private const val COLS_PARAMETER = "cols"

private fun addOptions(options: Options) {
    options.addOption(WEIGHT_PARAMETER, true, "Weights, rounded by quotation marks and separated by spaces");
    options.addOption(ROWS_PARAMETER, true, "Number of rows in the pallete");
    options.addOption(COLS_PARAMETER, true, "Number of columns in the pallete");
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

    val (c, r) = getPalleteDimensions()
    val normalizedPallet = getNormalizedPallete(r, c)

    return Pair(normalizedPallet, PalleteDimensions(r, c))
}

private fun getEnvironment(line: CommandLine): PalleteEnvironment {
    val (normalizedPallet, palleteDimensions) = getPallete(
            line.getOptionValue(WEIGHT_PARAMETER).trim().split(Regex(" +")).map { Integer.parseInt(it) },
            line.getOptionValue(ROWS_PARAMETER, "0").toInt(),
            line.getOptionValue(COLS_PARAMETER, "0").toInt())

    return PalleteEnvironment(normalizedPallet, palleteDimensions, //
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt())

}

fun main(args: Array<String>) {
    executeMain(
            args,
            ::addOptions,
            ::validateParameters,
            ::getEnvironment,
            true,
            { p, e -> p.addListener(e as PalleteEnvironment) }
    )
}

