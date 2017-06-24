package rafael.ktgenetic.sudoku

import org.apache.commons.cli.CommandLine
import rafael.ktgenetic.Environment
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.console.CHILDREN_TO_SURVIVE_PARAMETER
import rafael.ktgenetic.console.GENERATIONS_PARAMETER
import rafael.ktgenetic.console.executeMain

private fun getEnvironment(line: CommandLine): SudokuEnvironment {
    return SudokuEnvironment(
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt(),
            mutationFactor = 0.02)
}

private fun showEnvironmentDetails(environment: Environment<Cell, Puzzle>): String = ""

fun main(args: Array<String>) = executeMain(
        args,
        {},
        {},
        ::getEnvironment,
        GeneticProcessorChoice.ORDERED,
        ::showEnvironmentDetails
)