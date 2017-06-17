package rafael.ktgenetic.sudoku

import org.apache.commons.cli.CommandLine
import rafael.ktgenetic.CHILDREN_TO_SURVIVE_PARAMETER
import rafael.ktgenetic.GENERATIONS_PARAMETER
import rafael.ktgenetic.executeMain

private fun getEnvironment(line: CommandLine): SudokuEnvironment {
    return SudokuEnvironment(
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt(),
            mutationFactor = 0.02)
}

fun main(args: Array<String>) {
    executeMain(
            args,
            {},
            {},
            ::getEnvironment, true
    )
}