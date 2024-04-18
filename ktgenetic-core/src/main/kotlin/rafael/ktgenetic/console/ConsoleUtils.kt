package rafael.ktgenetic.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import rafael.ktgenetic.core.utils.configureLog
import rafael.ktgenetic.core.selection.SelectionOperatorChoice

/**
 * Constants for command line parameters.
 */
const val GENERATIONS_PARAMETER = "g"
const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
const val HELPER_PARAMETER = "h"
const val LOG_LEVEL_PARAMETER = "l"
const val NO_STOP_PROCESSING_PARAMETER = "p"
const val SELECTION_STRATEGY_PARAMETER = "s"
const val ADD_MUTATION_TUNER_PARAMETER = "m"

/**
 * Extension function to get an integer option value from the command line.
 *
 * @param opt The option to get the value for.
 * @param defaultValue The default value to use if the option is not present.
 * @return The integer value of the option.
 */
fun CommandLine.getIntOptionValue(opt: String, defaultValue: Int): Int =
        this.getOptionValue(opt, defaultValue.toString()).toInt()

/**
 * Extension function to get the max generations option value from the command line.
 *
 * @param value The default value to use if the option is not present.
 * @return The integer value of the max generations option.
 */
fun CommandLine.getMaxGenerations(value: Int = 100) = this.getIntOptionValue(GENERATIONS_PARAMETER, value)

/**
 * Extension function to get the population by generation option value from the command line.
 *
 * @param value The default value to use if the option is not present.
 * @return The integer value of the population by generation option.
 */
fun CommandLine.getPopulationByGeneration(value: Int = 100) = this.getIntOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, value)

/**
 * Function to get the command line options.
 *
 * @param additionalOptions Function to add additional options.
 * @return The command line options.
 */
fun getOptions(additionalOptions: (Options) -> Unit): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(GENERATIONS_PARAMETER, true, "Max number of generations (Default = 100)")
    options.addOption(CHILDREN_TO_SURVIVE_PARAMETER, true, "Quantity of Children to survive to next generation")
    options.addOption(LOG_LEVEL_PARAMETER, true, "Log Level: 1 = DEBUG, 2 = TRACE, 3 = TRACER (Default INFO)")
    options.addOption(NO_STOP_PROCESSING_PARAMETER, false, "Process with no console interaction")
    options.addOption(SELECTION_STRATEGY_PARAMETER, true, "Selection operator to be used. " +
            "Values: ${SelectionOperatorChoice.entries.joinToString { it.code + " = " + it.description }} " +
            "(Default: ${SelectionOperatorChoice.TRUNCATE.code})")
    options.addOption(ADD_MUTATION_TUNER_PARAMETER, false, "Add Mutation tuner")

    additionalOptions(options)

    return options
}

/**
 * Function to show the command line options.
 *
 * @param options The command line options.
 */
fun showOptions(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("Main", options)
}

/**
 * Function to configure the log level.
 *
 * @param line The parsed command line arguments.
 */
fun configureLogLevel(line: CommandLine) {
    if (line.hasOption(LOG_LEVEL_PARAMETER)) {
        val logLevel = line.getOptionValue(LOG_LEVEL_PARAMETER)
        configureLog(logLevel)
    }
}

