package rafael.ktgenetic.nqueens.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.nqueens.Board
import rafael.ktgenetic.nqueens.BoardEnvironment
import rafael.ktgenetic.nqueens.Piece
import rafael.ktgenetic.core.processor.GeneticCrossingType
import java.lang.Exception

private const val SIZE_PARAMETER = "size"

private const val PIECE_PARAMETER = "piece"

private var isQueens: Boolean = true

private fun addOptions(options: Options) {
    options.addOption(SIZE_PARAMETER, true, "Board Size")
    options.addOption(PIECE_PARAMETER, true, "Piece (QUEEN or BISHOP)")
}

private fun validateParameters(line: CommandLine) {
    require(line.hasOption(SIZE_PARAMETER)) { "Please provide the board size" }
    require(line.getOptionValue(SIZE_PARAMETER).matches(Regex("^\\d+$"))) {
        "Board Size incorrect: \"${line.getOptionValue(SIZE_PARAMETER)}\"; Please provide a positive Number"
    }

    require(line.hasOption(PIECE_PARAMETER)) { "Please provide the Piece (QUEEN or BISHOP)" }
    require(Piece.values().map { p -> p.toString() }.contains(line.getOptionValue(PIECE_PARAMETER))) {
        "Piece must be QUEEN or BISHOP"
    }
    isQueens = line.getOptionValue(PIECE_PARAMETER) == Piece.QUEEN.toString()
}

private fun getEnvironment(line: CommandLine): BoardEnvironment =
        BoardEnvironment(line.getOptionValue(SIZE_PARAMETER).toInt(),
                Piece.valueOf(line.getOptionValue(PIECE_PARAMETER)),
                line.getMaxGenerations(),
                line.getPopulationByGeneration())

private fun showEnvironmentDetails(environment: Environment<Int, Board>): String =
        "Board Size: ${(environment as BoardEnvironment).boardSize}"

class FinalBoardListener : ProcessorListener {

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.ENDED_BY_FITNESS) {
            val finalBoard = event.population[0] as Board

            println("================== FINAL BOARD ==================")
            Board.printBoard(finalBoard)
            println("=================================================")
        }
    }

}

fun getProcessorChoice(args: Array<String>): GeneticCrossingType {
    var result = GeneticCrossingType.SIMPLE
    val idx = args.indexOf("-$PIECE_PARAMETER")
    if (idx >= 0) {
        try {
            @Suppress("NON_EXHAUSTIVE_WHEN")
            when (Piece.QUEEN) {
                Piece.valueOf(args[idx + 1]) -> result = GeneticCrossingType.ORDERED
            }
        } catch (_: Exception) {
        }
    }

    return result
}

fun main(args: Array<String>) =
        executeMain(
                args,
                ::addOptions,
                ::validateParameters,
                ::getEnvironment,
                getProcessorChoice(args),
                ::showEnvironmentDetails
        ) { p, _ -> p.addListener(FinalBoardListener()) }


