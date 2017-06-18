package rafael.ktgenetic

import org.apache.commons.cli.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import kotlin.system.measureTimeMillis

val TRACER: Level = Level.forName("TRACER", 700)

const val GENERATIONS_PARAMETER = "g"
const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
const val HELPER_PARAMETER = "h"
const val LOG_LEVEL_PARAMETER = "l"
const val NO_STOP_PROCESSING_PARAMETER = "p"
const val SELECTION_STRATEGY_PARAMETER = "s"
const val ADD_MUTATION_TUNER_PARAMETER = "m"

const val ROULETTE_STRATEGY_PARAMETER = "r"
const val GREATEST_FITNESS_STRATEGY_PARAMETER = "g"

val mainLogger = LogManager.getLogger("Main")!!

fun getOptions(additionalOptions: (Options) -> Unit): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)")
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation")
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE, 3 = TRACER (Default INFO)")
    options.addOption(NO_STOP_PROCESSING_PARAMETER, false, "Process with no console interaction")
    options.addOption(SELECTION_STRATEGY_PARAMETER, true, "Selection strategy to be used. " +
            "Values: r = Roulette, g = Greatest. (Default: g)")
    options.addOption(ADD_MUTATION_TUNER_PARAMETER, false, "Add Mutation tuner")

    additionalOptions(options)

    return options
}

fun showOptions(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("Main", options)
}

fun configureLogLevel(line: org.apache.commons.cli.CommandLine) {
    if (line.hasOption(LOG_LEVEL_PARAMETER)) {
        /**
         * Code extracted from https://stackoverflow.com/questions/23434252/programmatically-change-log-level-in-log4j2
         */
        val ctx = LogManager.getContext(false) as LoggerContext
        val config = ctx.configuration
        val loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)

        val logLevel = line.getOptionValue(LOG_LEVEL_PARAMETER)
        when (logLevel) {
            "1" -> loggerConfig.level = Level.DEBUG
            "2" -> loggerConfig.level = Level.TRACE
            "3" -> loggerConfig.level = TRACER
            else -> mainLogger.warn("Log level unrecognised: $logLevel")
        }
        ctx.updateLoggers()  // This causes all Loggers to refetch information from their LoggerConfig.
    }
}

fun <C : Chromosome<*>> configureSelectionStrategy(line: CommandLine, environment: Environment<*, C>): SelectionStrategy<C> {
    val strategyParameter = line.getOptionValue(SELECTION_STRATEGY_PARAMETER, GREATEST_FITNESS_STRATEGY_PARAMETER)

    val strategy: SelectionStrategy<C>
    when (strategyParameter) {
        GREATEST_FITNESS_STRATEGY_PARAMETER -> strategy = GreatestFitnessSelectionStrategy<C>(environment.generationSize)
        ROULETTE_STRATEGY_PARAMETER -> strategy = RouletteElitismSelectionStrategy<C>(environment.generationSize)
        else -> throw IllegalArgumentException("Invalid selection strategy parameter '$strategyParameter'")
    }

    return strategy
}

fun <G, C : Chromosome<G>> executeMain(
        args: Array<String>,
        additionalOptions: (Options) -> Unit,
        validateParameters: (CommandLine) -> Unit,
        getEnvironment: (CommandLine) -> Environment<G, C>,
        useOrdered: Boolean = false,
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
            val selectionStrategy = configureSelectionStrategy(line, environment)

            val processor =
                    if (useOrdered) OrderedGeneticProcessor(environment, selectionStrategy)
                    else GeneticProcessor(environment, selectionStrategy)
            processor.addListener(LogProcessorListener<G, C>())
            if (!line.hasOption(NO_STOP_PROCESSING_PARAMETER)) {
                processor.addListener(ConsoleProcessorListener(processor))
            }
            if(line.hasOption(ADD_MUTATION_TUNER_PARAMETER)) {
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