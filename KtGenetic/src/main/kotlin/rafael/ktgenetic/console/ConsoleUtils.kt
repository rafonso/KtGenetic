package rafael.ktgenetic.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import rafael.ktgenetic.TRACER
import rafael.ktgenetic.selection.SelectionStrategyChoice

const val GENERATIONS_PARAMETER = "g"
const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
const val HELPER_PARAMETER = "h"
const val LOG_LEVEL_PARAMETER = "l"
const val NO_STOP_PROCESSING_PARAMETER = "p"
const val SELECTION_STRATEGY_PARAMETER = "s"
const val ADD_MUTATION_TUNER_PARAMETER = "m"

val mainLogger = LogManager.getLogger("Main")!!

fun CommandLine.getIntOptionValue(opt: String, defaultValue: Int): Int =
        this.getOptionValue(opt, defaultValue.toString()).toInt()

fun CommandLine.getMaxGenerations(value: Int = 100) = this.getIntOptionValue(GENERATIONS_PARAMETER, value)

fun CommandLine.getPopulationByGeneration(value: Int = 100) = this.getIntOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, value)

fun getOptions(additionalOptions: (Options) -> Unit): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)")
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation")
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE, 3 = TRACER (Default INFO)")
    options.addOption(NO_STOP_PROCESSING_PARAMETER, false, "Process with no console interaction")
    options.addOption(SELECTION_STRATEGY_PARAMETER, true, "Selection strategy to be used. " +
            "Values: ${SelectionStrategyChoice.values().joinToString { it.code + " = " + it.description }} " +
            "(Default: ${SelectionStrategyChoice.TRUNCATE.code})")
    options.addOption(ADD_MUTATION_TUNER_PARAMETER, false, "Add Mutation tuner")

    additionalOptions(options)

    return options
}

fun showOptions(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("Main", options)
}

fun configureLogLevel(line: CommandLine) {
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
