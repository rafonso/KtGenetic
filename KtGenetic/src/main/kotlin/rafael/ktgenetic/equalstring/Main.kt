package rafael.ktgenetic.equalstring

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import rafael.ktgenetic.GeneticProcessor


private const val WORD_PARAMETER = "w"
private const val GENERATIONS_PARAMETER = "g"
private const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
private const val HELPER_PARAMETER = "h"
private const val LOG_LEVEL_PARAMETER = "l"

private val log: Logger = LoggerFactory.getLogger("Main")

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
/*
private fun getProcessorParameters(line: org.apache.commons.cli.CommandLine) = ProcessorParameters(
        line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(), //
        line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt()
)
*/
private fun getEnvironment(line: org.apache.commons.cli.CommandLine) =
        EqualStringEnvironment(
                line.getOptionValue(WORD_PARAMETER), //
                line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt(),
                line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt())

private fun configureLogLevel(line: org.apache.commons.cli.CommandLine) {
    val logLevel = line.getOptionValue(LOG_LEVEL_PARAMETER, "")
    when (logLevel) {
        "1" -> org.apache.log4j.Logger.getRootLogger().level = org.apache.log4j.Level.DEBUG
        "2" -> org.apache.log4j.Logger.getRootLogger().level = org.apache.log4j.Level.TRACE
        else -> log.warn("Log level unrecognised: {}.", logLevel)
    }
}


fun main(args: Array<String>) {
    val t0 = System.currentTimeMillis()

    val options = getOptions()

    try {
        val parser: org.apache.commons.cli.CommandLineParser = org.apache.commons.cli.DefaultParser()
        val line = parser.parse(options, args);

        if (line.hasOption(HELPER_PARAMETER)) {
            showOptions(options);
            return;
        }
        validateParameters(line)
        configureLogLevel(line)

        val word = line.getOptionValue(WORD_PARAMETER)
        val environment = getEnvironment(line)

        val processor = GeneticProcessor<String>()
        processor.addListener(rafael.ktgenetic.LogProcessorListener<String>())
        processor.addListener(rafael.ktgenetic.ConsoleProcessorListener())
        val result = processor.process(environment)

        log.info("Result: {}", result)
        log.info("Finished. Time: {} ms", (System.currentTimeMillis() - t0))
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
