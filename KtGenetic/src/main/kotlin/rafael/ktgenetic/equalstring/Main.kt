package rafael.ktgenetic.equalstring

import org.apache.commons.cli.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import rafael.ktgenetic.ConsoleProcessorListener
import rafael.ktgenetic.GeneticProcessor
import rafael.ktgenetic.LogProcessorListener
import rafael.ktgenetic.TRACER
import kotlin.system.measureTimeMillis


private const val WORD_PARAMETER = "w"
private const val GENERATIONS_PARAMETER = "g"
private const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
private const val HELPER_PARAMETER = "h"
private const val LOG_LEVEL_PARAMETER = "l"

private val log: Logger = LogManager.getLogger("Main");

private fun getOptions(): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(WORD_PARAMETER, true, "Word");
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)");
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation");
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE (Default INFO)");

    return options
}

private fun showOptions(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("Main", options)
}

private fun validateParameters(line: CommandLine) {
    if (!line.hasOption(WORD_PARAMETER)) {
        throw IllegalArgumentException("Please provide the word to be processed")
    }
}

private fun getEnvironment(line: org.apache.commons.cli.CommandLine) =
        EqualStringEnvironment(
                line.getOptionValue(WORD_PARAMETER), //
                line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt(),
                line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt())

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
            else -> log.warn("Log level unrecognised: {}.", logLevel)
        }
        ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
    }
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
        validateParameters(line)
        configureLogLevel(line)

        val executionTime = measureTimeMillis {
            val environment = getEnvironment(line)

            val processor = GeneticProcessor<Char, Word>(environment)
            processor.addListener(LogProcessorListener<Char, Word>())
            processor.addListener(ConsoleProcessorListener<Char, Word>(processor))
            val result = processor.process()

            log.info("Result: {}", result)
        }
        log.info("Finished. Time: $executionTime ms")
    } catch (e: org.apache.commons.cli.ParseException) {
        log.error("Invalid Command Line: " + e.message, e)
        showOptions(options);
    } catch (e: IllegalArgumentException) {
        log.error(e.message)
        showOptions(options);
    } catch (e: Exception) {
        log.error(e.message, e)
    }
}
