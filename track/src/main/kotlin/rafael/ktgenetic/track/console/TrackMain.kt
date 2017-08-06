package rafael.ktgenetic.track.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.Environment
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getIntOptionValue
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.track.Direction
import rafael.ktgenetic.track.Path
import rafael.ktgenetic.track.TrackEnvironment

private const val FACTOR_PARAMETER = "f"
private const val HEIGHT_PARAMETER = "he"
private const val WIDTH_PARAMETER = "wi"

private const val HEIGHT_DEFAULT = 5
private const val WIDTH_DEFAULT = 5
private const val FACTOR_DEFAULT = 1.0

private fun addOptions(options: Options) {
    options.addOption(HEIGHT_PARAMETER, true, "Height. Default $HEIGHT_DEFAULT")
    options.addOption(WIDTH_PARAMETER, true, "Width. Default $WIDTH_DEFAULT")
    options.addOption(FACTOR_PARAMETER, true, "Factor to be used in the product of height and width. Default $FACTOR_DEFAULT")
}

private fun validateParameters(line: CommandLine) {
    if (!line.hasOption(HEIGHT_PARAMETER)) {
        throw IllegalArgumentException("Please provide the height parameter")
    }
    if (!line.getOptionValue(HEIGHT_PARAMETER).matches(Regex("^\\d+$"))) {
        throw IllegalArgumentException("Height parameter not numeric: " + line.getOptionValue(HEIGHT_PARAMETER))
    }
    if (!line.hasOption(WIDTH_PARAMETER)) {
        throw IllegalArgumentException("Please provide the width parameter")
    }
    if (!line.getOptionValue(WIDTH_PARAMETER).matches(Regex("^\\d+$"))) {
        throw IllegalArgumentException("Width parameter not numeric: " + line.getOptionValue(WIDTH_PARAMETER))
    }
    if (line.hasOption(FACTOR_PARAMETER) &&
            !line.getOptionValue(FACTOR_PARAMETER).matches(Regex("^\\d+\\.\\d+$"))) {
        throw IllegalArgumentException("FActor parameter not numeric: " + line.getOptionValue(FACTOR_PARAMETER))
    }
}

private fun getEnvironment(line: CommandLine): TrackEnvironment {
    val width = line.getIntOptionValue(WIDTH_PARAMETER, WIDTH_DEFAULT)
    val height = line.getIntOptionValue(HEIGHT_PARAMETER, HEIGHT_DEFAULT)
    val factor = line.getOptionValue(FACTOR_PARAMETER, FACTOR_DEFAULT.toString()).toDouble()
    val trackSize = (width * height * factor).toInt()

    return TrackEnvironment(width,
            height,
            trackSize, //
            line.getMaxGenerations(),
            line.getPopulationByGeneration(),
            1.0
    )
}

private fun showEnvironmentDetails(environment: Environment<Direction, Path>): String {
    val esEnvironment = environment as TrackEnvironment

    return "Width: ${esEnvironment.width}, Height: ${esEnvironment.height}, Track size: ${esEnvironment.trackSize}"
}

fun main(args: Array<String>) = executeMain(
        args,
        ::addOptions,
        ::validateParameters,
        ::getEnvironment,
        GeneticProcessorChoice.SIMPLE,
        ::showEnvironmentDetails
)

