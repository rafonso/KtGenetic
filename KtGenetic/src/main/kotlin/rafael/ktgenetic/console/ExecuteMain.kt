package rafael.ktgenetic.console

import org.apache.commons.cli.*
import rafael.ktgenetic.*
import rafael.ktgenetic.processor.GeneticProcessor
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.selection.SelectionStrategyChoice
import rafael.ktgenetic.selection.chooseStrategy
import kotlin.system.measureTimeMillis

fun <G, C : Chromosome<G>> executeMain(
        args: Array<String>,
        additionalOptions: (Options) -> Unit,
        validateParameters: (CommandLine) -> Unit,
        getEnvironment: (CommandLine) -> Environment<G, C>,
        processorChoice: GeneticProcessorChoice,
        prepareProcessing: (GeneticProcessor<G, C>, Environment<G, C>) -> Unit = { _, _ -> }) {
    val options = getOptions(additionalOptions)

    try {
        val parser: CommandLineParser = DefaultParser()
        val line = parser.parse(options, args)

        if (line.hasOption(HELPER_PARAMETER)) {
            showOptions(options)
            return
        }
        validateParameters(line)
        configureLogLevel(line)

        val executionTime = measureTimeMillis {
            val environment = getEnvironment(line)
            val selectionStrategy = chooseStrategy<C>(
                    line.getOptionValue(SELECTION_STRATEGY_PARAMETER, SelectionStrategyChoice.TRUNCATE.code),
                    environment.generationSize)

            val processor = processorChoice.newInstance(environment, selectionStrategy)
            processor.addListener(LogProcessorListener<G, C>())
            if (!line.hasOption(NO_STOP_PROCESSING_PARAMETER)) {
                processor.addListener(ConsoleProcessorListener(processor))
            }
            if (line.hasOption(ADD_MUTATION_TUNER_PARAMETER)) {
                processor.addListener(MutationTuner(environment))
            }

            prepareProcessing(processor, environment)
            mainLogger.info("Max Generations: ${environment.maxGenerations}, " +
                    "Generation Size: ${environment.generationSize}, " +
                    "Selection Strategy: ${selectionStrategy.javaClass.simpleName}")

            val result = processor.process()

            mainLogger.info("Result: $result")
        }
        mainLogger.info("Finished. Time: $executionTime ms")
    } catch (e: ParseException) {
        mainLogger.error(e.message)
        showOptions(options)
    } catch (e: IllegalArgumentException) {
        mainLogger.error(e.message)
        showOptions(options)
    } catch (e: Exception) {
        mainLogger.error(e.message, e)
    }
}