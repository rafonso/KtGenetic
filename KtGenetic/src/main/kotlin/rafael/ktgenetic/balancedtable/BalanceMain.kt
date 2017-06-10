package rafael.ktgenetic.balancedtable

import org.apache.commons.cli.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import rafael.ktgenetic.*
import kotlin.system.measureTimeMillis

private const val WEIGHT_PARAMETER = "w"
private const val GENERATIONS_PARAMETER = "g"
private const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
private const val HELPER_PARAMETER = "h"
private const val LOG_LEVEL_PARAMETER = "l"

private val log: Logger = LogManager.getLogger("Main");

private fun getOptions(): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(WEIGHT_PARAMETER, true, "Weights, rounded by quotation marks and separated by spaces");
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)");
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation");
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE, 3 = TRACEST (Default INFO)");

    return options
}

private fun showOptions(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("Main", options)
}

private fun validateParameters(line: CommandLine) {
    if (!line.hasOption(WEIGHT_PARAMETER)) {
        throw IllegalArgumentException("Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
    }
    if (!line.getOptionValue(WEIGHT_PARAMETER).matches(Regex("^((\\d+) *)+$"))) {
        throw IllegalArgumentException("Weights incorrect: \"${line.getOptionValue(WEIGHT_PARAMETER)}\"; " +
                "Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
    }
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

private fun getEnvironment(line: CommandLine): BalanceEnvironment {
    val weights = line.getOptionValue(WEIGHT_PARAMETER).trim().split(Regex(" +")).map { Integer.parseInt(it) }
    println(weights)
    return BalanceEnvironment(
            weights, //
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "1000000").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt())
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

            val processor = OrderedGeneticProcessor<Box, Balance>(environment)
            processor.addListener(LogProcessorListener<Box, Balance>())
            processor.addListener(ConsoleProcessorListener<Box, Balance>(processor))
            processor.addListener(environment)
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

