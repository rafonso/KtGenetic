package rafael.ktgenetic.sudoku

import org.apache.commons.cli.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import rafael.ktgenetic.ConsoleProcessorListener
import rafael.ktgenetic.LogProcessorListener
import rafael.ktgenetic.OrderedGeneticProcessor
import rafael.ktgenetic.TRACER
import kotlin.system.measureTimeMillis

private const val GENERATIONS_PARAMETER = "g"
private const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
private const val HELPER_PARAMETER = "h"
private const val LOG_LEVEL_PARAMETER = "l"

private val log: Logger = LogManager.getLogger("Main");

private fun getOptions(): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)");
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation");
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE, 3 = TRACEST (Default INFO)");

    return options
}

private fun showOptions(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("Main", options)
}

private fun configureLogLevel(line: org.apache.commons.cli.CommandLine) {
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
            else -> log.warn("Log level unrecognised: $logLevel")
        }
        ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
    }
}

private fun getEnvironment(line: CommandLine): SudokuEnvironment {
    return SudokuEnvironment(
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt(),
            mutationFactor = 0.02)
}

fun main(args: Array<String>) {
    val options = getOptions()

    try {
        val parser: CommandLineParser = DefaultParser()
        val line = parser.parse(options, args);

        if (line.hasOption(HELPER_PARAMETER)) {
            showOptions(options);
            return;
        }
        configureLogLevel(line)

        val executionTime = measureTimeMillis {
            val environment = getEnvironment(line)

            val processor = OrderedGeneticProcessor<Cell, Puzzle>(environment)
            processor.addListener(LogProcessorListener<Cell, Puzzle>())
            processor.addListener(ConsoleProcessorListener<Cell, Puzzle>(processor))

            val result = processor.process()

            log.info("Result: {}", result)
            log.info("Better result:\n${result[0].formatted}")
        }

        log.info("Finished. Time: $executionTime ms")
    } catch (e: ParseException) {
        log.error("Invalid Command Line: " + e.message, e)
        showOptions(options);
    } catch (e: IllegalArgumentException) {
        log.error(e.message)
        showOptions(options);
    } catch (e: Exception) {
        log.error(e.message, e)
    }
}