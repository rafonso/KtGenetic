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
const val NO_STOP_PROCESSING_PARAMETER = "s"

val mainLogger = LogManager.getLogger("Main");

fun getOptions(additionalOptions: (Options) -> Unit): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)");
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation");
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE, 3 = TRACEST (Default INFO)");
    options.addOption(NO_STOP_PROCESSING_PARAMETER, false, "Process with no console iteraction");

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
            else -> mainLogger.warn("Log level unrecognised: {}.", logLevel)
        }
        ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
    }
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
        val line = parser.parse(options, args);

        if (line.hasOption(HELPER_PARAMETER)) {
            showOptions(options);
            return;
        }
        validateParameters(line)
        configureLogLevel(line)

        val executionTime = measureTimeMillis {
            val environment = getEnvironment(line)

            val processor =
                    if (useOrdered) OrderedGeneticProcessor<G, C>(environment)
                    else GeneticProcessor<G, C>(environment)
            processor.addListener(LogProcessorListener<G, C>())
            if (!line.hasOption(NO_STOP_PROCESSING_PARAMETER)) {
                processor.addListener(ConsoleProcessorListener<G, C>(processor))
            }

            prepareProcessing(processor, environment)

            val result = processor.process()

            mainLogger.info("Result: {}", result)
        }
        mainLogger.info("Finished. Time: $executionTime ms")
    } catch (e: org.apache.commons.cli.ParseException) {
        mainLogger.error("Invalid Command Line: " + e.message, e)
        showOptions(options);
    } catch (e: IllegalArgumentException) {
        mainLogger.error(e.message)
        showOptions(options);
    } catch (e: Exception) {
        mainLogger.error(e.message, e)
    }
}