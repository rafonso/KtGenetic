package rafael.ktgenetic.sudoku

import org.apache.commons.cli.CommandLine
import rafael.ktgenetic.Environment
import rafael.ktgenetic.console.*
import rafael.ktgenetic.processor.GeneticProcessorChoice

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