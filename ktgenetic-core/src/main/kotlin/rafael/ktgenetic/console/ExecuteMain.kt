package rafael.ktgenetic.console

import org.apache.commons.cli.*
import rafael.ktgenetic.core.*
import rafael.ktgenetic.core.processor.GeneticCrossingType
import rafael.ktgenetic.core.processor.GeneticProcessor
import rafael.ktgenetic.core.selection.SelectionOperator
import rafael.ktgenetic.core.selection.SelectionOperatorChoice
import rafael.ktgenetic.core.selection.codeToSelectionOperatorChoice
import rafael.ktgenetic.core.utils.LogProcessorListener
import rafael.ktgenetic.core.utils.mainLogger
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

/**
 * Calls the execution of a [GeneticProcessor] in console.
 *
 * @param args Command line arguments
 * @param additionalOptions Function that parse specific command line arguments to [GeneticProcessor]
 * @param validateParameters Function that validate the command line arguments
 * @param getEnvironment Function that returns specific [Environment] from command line arguments
 * @param geneticCrossingType [GeneticCrossingType] to be used
 * @param showEnvironmentDetails Function that returns a String showing the specific command line arguments
 * @param prepareProcessing Function that receives the GeneticProcessor and Environment to be executed to make some preparation.
 */
fun <G, C : Chromosome<G>> executeMain(
    args: Array<String>,
    additionalOptions: (Options) -> Unit,
    validateParameters: (CommandLine) -> Unit,
    getEnvironment: (CommandLine) -> Environment<G, C>,
    geneticCrossingType: GeneticCrossingType,
    showEnvironmentDetails: (Environment<G, C>) -> String,
    prepareProcessing: (GeneticProcessor<G, C>, Environment<G, C>) -> Unit = { _, _ -> }
) {
    val options = getOptions(additionalOptions)

    try {
        val line = getCommandLineArguments(args, options, validateParameters)
        configureLogLevel(line)

        val executionTime = measureTimeMillis {
            val environment = getEnvironment(line)
            val selectionStrategy = codeToSelectionOperatorChoice(
                line.getOptionValue(SELECTION_STRATEGY_PARAMETER, SelectionOperatorChoice.TRUNCATE.code)
            ).chooseSelectionOperator(environment, elitism = true, allowRepetition = true)

            val processor = prepareProcessor(line, environment, selectionStrategy, geneticCrossingType)

            prepareProcessing(processor, environment)
            mainLogger.info(
                "Max Generations: ${environment.maxGenerations}, " +
                        "Generation Size: ${environment.generationSize}, " +
                        "Selection Strategy: $selectionStrategy"
            )
            mainLogger.info(showEnvironmentDetails(environment))

            val result = processor.process()

            mainLogger.info("Result: $result")
        }
        mainLogger.info("Finished. Time: $executionTime ms")
    } catch (e: ParseException) {
        mainLogger.error(e.message)
        showOptions(options)
    } catch (e: IllegalArgumentException) {
        mainLogger.error(e.message, e)
        showOptions(options)
    } catch (e: Exception) {
        mainLogger.error(e.message, e)
    }
}

private fun <C : Chromosome<G>, G> prepareProcessor(
    line: CommandLine,
    environment: Environment<G, C>,
    selectionStrategy: SelectionOperator<C>,
    processorChoice: GeneticCrossingType
): GeneticProcessor<G, C> {
    val processor = GeneticProcessor(processorChoice, environment, selectionStrategy)

    processor.addListener(LogProcessorListener())
    if (!line.hasOption(NO_STOP_PROCESSING_PARAMETER)) {
        processor.addListener(ConsoleProcessorListener(processor))
    }
    if (line.hasOption(ADD_MUTATION_TUNER_PARAMETER)) {
        processor.addListener(MutationTuner(environment))
    }

    return processor
}

private fun getCommandLineArguments(
    args: Array<String>,
    options: Options,
    validateParameters: (CommandLine) -> Unit
): CommandLine {
    val line = DefaultParser().parse(options, args)

    if (line.hasOption(HELPER_PARAMETER)) {
        showOptions(options)
        exitProcess(1)
    }
    validateParameters(line)

    return line
}
