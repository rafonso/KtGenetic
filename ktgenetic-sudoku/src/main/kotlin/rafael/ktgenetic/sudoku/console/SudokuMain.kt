package rafael.ktgenetic.sudoku.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.console.executeMain
import rafael.ktgenetic.console.getMaxGenerations
import rafael.ktgenetic.console.getPopulationByGeneration
import rafael.ktgenetic.core.processor.GeneticCrossingType
import rafael.ktgenetic.sudoku.Grid
import rafael.ktgenetic.sudoku.MutationStrategy
import rafael.ktgenetic.sudoku.PuzzleEnvironment
import rafael.ktgenetic.sudoku.Row
import tornadofx.isInt

private const val SIZE_PARAMETER = "size"

private const val STRATEGY_PROPERTY = "strategy"

private fun addOptions(options: Options) {
    options.addOption(SIZE_PARAMETER, true, "Grid Size (must be a perfect square)")
    options.addOption(
        STRATEGY_PROPERTY,
        true,
        "Row mutation strategy ${
            MutationStrategy.entries.joinToString(
            prefix = "(",
            postfix = ")",
            transform = MutationStrategy::name
        )}"
    )
}

private fun validateParameters(line: CommandLine) {
    require(line.hasOption(SIZE_PARAMETER)) { "Please provide the grid size" }
    require(line.getOptionValue(SIZE_PARAMETER).isInt()) {
        "Gird Size incorrect: \"${line.getOptionValue(SIZE_PARAMETER)}\"; Please provide a positive Number"
    }

    require(line.hasOption(STRATEGY_PROPERTY)) { "Please provide the Mutation strategy" }
    require(MutationStrategy.entries.map { m -> m.name }.contains(line.getOptionValue(STRATEGY_PROPERTY))) {
        "Mutation strategy must be ${
            MutationStrategy.entries.joinToString(
            prefix = "",
            postfix = "",
            separator = " or ",
            transform = MutationStrategy::name
        )}"
    }
}

private fun getEnvironment(line: CommandLine): PuzzleEnvironment =
    PuzzleEnvironment(
        line.getOptionValue(SIZE_PARAMETER).toInt(),
        MutationStrategy.valueOf(line.getOptionValue(STRATEGY_PROPERTY)),
        line.getMaxGenerations(),
        line.getPopulationByGeneration())

private fun showEnvironmentDetails(environment: Environment<Row, Grid>): String =
    with(environment as PuzzleEnvironment) {
        "Grid Size: $gridSize, Strategy: $strategy"
    }

class FinalGridListener : ProcessorListener {

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.ENDED_BY_FITNESS) {
            val finalGrid = event.population[0] as Grid

            println("================== FINAL GRID ===================")
            println(Grid.formatGrid(finalGrid))
            println("=================================================")
        }
    }
}

fun main(args: Array<String>) =
    executeMain(
        args,
        ::addOptions,
        ::validateParameters,
        ::getEnvironment,
        GeneticCrossingType.SIMPLE,
        ::showEnvironmentDetails
    ) { p, _ -> p.addListener(FinalGridListener()) }


