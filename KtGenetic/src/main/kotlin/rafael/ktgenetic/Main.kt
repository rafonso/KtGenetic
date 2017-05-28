package rafael.ktgenetic

import org.apache.commons.cli.*
import org.apache.log4j.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory


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

private fun getProcessorParameters(line: CommandLine) = ProcessorParameters(
        line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(), //
        line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt()
)

private fun getParameters(line: CommandLine) =
        EqualStringParameters(
                line.getOptionValue(WORD_PARAMETER), //
                line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt(),
                line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt())

private fun configureLogLevel(line: CommandLine) {
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
        val parser: CommandLineParser = DefaultParser()
        val line = parser.parse(options, args);

        if (line.hasOption(HELPER_PARAMETER)) {
            showOptions(options);
            return;
        }
        validateParameters(line)
        configureLogLevel(line)

        val word = line.getOptionValue(WORD_PARAMETER)
        val parameters = getProcessorParameters(line)
        val geneticParameters = getParameters(line)

        val processor = GeneticProcessor<String>()
        processor.addListener(LogProcessorListener<String>())
        processor.addListener(ConsoleProcessorListener())
        val result = processor.process(word, geneticParameters)

        log.info("Result: {}", result)
        log.info("Finished. Time: {} ms", (System.currentTimeMillis() - t0))
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
