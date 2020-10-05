package rafael.ktgenetic.camouflage.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.Environment
import rafael.ktgenetic.camouflage.CamouflageEnvironment
import rafael.ktgenetic.camouflage.Kolor
import rafael.ktgenetic.camouflage.KolorDistance
import rafael.ktgenetic.camouflage.MAX_COLOR_VALUE
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getIntOptionValue
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.processor.GeneticCrossingType
import tornadofx.isInt

private const val R_PARAMETER = "r"
private const val G_PARAMETER = "e" // to not confuse with Max number of generations param (= "g")
private const val B_PARAMETER = "b"
private val colorsParams = listOf(Pair(R_PARAMETER, "Red"), Pair(G_PARAMETER, "Green"), Pair(B_PARAMETER, "Blue"))

private fun addOptions(options: Options) {
    colorsParams.forEach { options.addOption(it.first, true, "Background's ${it.second} value") }
}

private fun validateParameters(line: CommandLine) {
    colorsParams.forEach {
        require(line.hasOption(it.first)) { "Please provide the Background's ${it.second} value" }
        require(line.getOptionValue(it.first).isInt()) { "Background's ${it.second} value must be a Integer" }
        require(
            (0..MAX_COLOR_VALUE).contains(
                line.getIntOptionValue(
                    it.first,
                    -1
                )
            )
        ) { "Background's ${it.second} value must be a value between 0 and $MAX_COLOR_VALUE" }
    }
}

fun getBackgroundKolor(line: CommandLine) = Kolor(
    line.getIntOptionValue(R_PARAMETER, -1),
    line.getIntOptionValue(G_PARAMETER, -1),
    line.getIntOptionValue(B_PARAMETER, -1)
)

private fun getEnvironment(line: CommandLine) =
    CamouflageEnvironment(
        getBackgroundKolor(line),
        KolorDistance.RGB,
        line.getMaxGenerations(),
        line.getPopulationByGeneration()
    )

private fun showEnvironmentDetails(environment: Environment<Int, Kolor>): String {
    val cEnvironment = environment as CamouflageEnvironment

    return "Background: ${cEnvironment.backgroundColor}"
}

fun main(args: Array<String>) = executeMain(
    args,
    ::addOptions,
    ::validateParameters,
    ::getEnvironment,
    GeneticCrossingType.SIMPLE,
    ::showEnvironmentDetails
)
