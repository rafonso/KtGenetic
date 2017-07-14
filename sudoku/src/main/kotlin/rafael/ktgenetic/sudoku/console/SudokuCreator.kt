package rafael.ktgenetic.sudoku.console

import org.apache.commons.cli.CommandLine
import rafael.ktgenetic.Environment
import rafael.ktgenetic.console.*
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.sudoku.Cell
import rafael.ktgenetic.sudoku.Puzzle
import rafael.ktgenetic.sudoku.SudokuEnvironment

private fun getEnvironment(line: CommandLine): SudokuEnvironment = SudokuEnvironment(
        line.getMaxGenerations(),
        line.getPopulationByGeneration(),
        0.02)

private fun showEnvironmentDetails(environment: Environment<Cell, Puzzle>) = ""

fun main(args: Array<String>) = executeMain(
        args,
        {},
        {},
        ::getEnvironment,
        GeneticProcessorChoice.ORDERED,
        ::showEnvironmentDetails
)