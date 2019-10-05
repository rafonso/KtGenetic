package rafael.ktgenetic.nqueens.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.*
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.nqueens.Board
import rafael.ktgenetic.nqueens.BoardEnvironment
import rafael.ktgenetic.processor.GeneticProcessorChoice

private const val SIZE_PARAMETER = "size"

private fun addOptions(options: Options) {
    options.addOption(SIZE_PARAMETER, true, "Board Size")
}

private fun validateParameters(line: CommandLine) {
    require(line.hasOption(SIZE_PARAMETER)) { "Please provide the board size" }
    require(line.getOptionValue(SIZE_PARAMETER).matches(Regex("^\\d+$"))) {
        "Board Size incorrect: \"${line.getOptionValue(SIZE_PARAMETER)}\"; Please provide a positive Number"
    }
}

private fun getEnvironment(line: CommandLine): BoardEnvironment =
        BoardEnvironment(line.getOptionValue(SIZE_PARAMETER).toInt(), line.getMaxGenerations(),
                line.getPopulationByGeneration())

private fun showEnvironmentDetails(environment: Environment<Int, Board>): String =
        "Board Size: ${(environment as BoardEnvironment).boardSize}"

class FinalBoardListener : ProcessorListener {

    private val colors = arrayOf(' ', '\u2592')
    private val queen = 'Q'

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.ENDED_BY_FITNESS) {
            val finalBoard = event.population[0] as Board
            val oddHouses = IntRange(0, finalBoard.content.size - 1).map { colors[it % 2] }.joinToString("")
            val evenHouses = oddHouses.reversed()
            val rowNumberFormat = "%${finalBoard.content.size.toString().length}d \u2016"

            println("================== FINAL BOARD ==================")
            finalBoard.content.forEachIndexed { row, filledColumn ->
                val rowColors = (if (row % 2 == 0) oddHouses else evenHouses).toCharArray()
                rowColors[filledColumn] = queen
                println(rowColors.joinToString(separator = "|", prefix = rowNumberFormat.format(row), postfix = "‖"))
            }
            println("=================================================")
        }
    }
}


fun main(args: Array<String>) = executeMain(
        args,
        ::addOptions,
        ::validateParameters,
        ::getEnvironment,
        GeneticProcessorChoice.ORDERED,
        ::showEnvironmentDetails
) { p, _ -> p.addListener(FinalBoardListener()) }

