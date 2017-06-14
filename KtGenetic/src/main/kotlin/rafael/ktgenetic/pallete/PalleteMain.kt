package rafael.ktgenetic.pallete

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

private const val WEIGHT_PARAMETER = "w"
private const val ROWS_PARAMETER = "rows"
private const val COLS_PARAMETER = "cols"
private const val GENERATIONS_PARAMETER = "g"
private const val CHILDREN_TO_SURVIVE_PARAMETER = "c"
private const val HELPER_PARAMETER = "h"
private const val LOG_LEVEL_PARAMETER = "l"

private val log: Logger = LogManager.getLogger("Main");

private fun getOptions(): Options {
    val options = Options()

    options.addOption(HELPER_PARAMETER, false, "Prints Usage")
    options.addOption(WEIGHT_PARAMETER, true, "Weights, rounded by quotation marks and separated by spaces");
    options.addOption(ROWS_PARAMETER, true, "Number of rows in the pallete");
    options.addOption(COLS_PARAMETER, true, "Number of columns in the pallete");
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

fun getPallete(weights: Collection<Int>, rows: Int, cols: Int): Pair<List<Int>, PalleteDimensions> {

    fun getPalleteDimensions(): Pair<Int, Int> {
        if (rows == 0 && cols == 0) {
            val sqrtSize = Math.sqrt(weights.size.toDouble()).toInt()
            val side: Int = sqrtSize + (if (weights.size == sqrtSize * sqrtSize) 0 else 1)
            return Pair(side, side)
        }
        if (rows == 0 && cols != 0) {
            return Pair((weights.size / cols) + (if (weights.size % cols == 0) 0 else 1), cols)
        }
        if (rows != 0 && cols == 0) {
            return Pair(rows, (weights.size / rows) + (if (weights.size % rows == 0) 0 else 1))
        }

        if ((rows * cols) < weights.size) {
            throw IllegalArgumentException("Number of Rows ($rows) times the number of columns ($cols) " +
                    "lesser than the size of weights (${weights.size})")
        }

        return Pair(rows, cols)
    }

    fun getNormalizedPallete(r: Int, c: Int): List<Int> {
        if (weights.size == r * c) {
            return weights.toList()
        }

        val result = IntArray(c * r)
        weights.forEachIndexed { index, w -> result[index] = w }
        return result.toList()
    }

    val (c, r) = getPalleteDimensions()
    val normalizedPallet = getNormalizedPallete(r, c)

    return Pair(normalizedPallet, PalleteDimensions(r, c))
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

            val processor = OrderedGeneticProcessor<Box, Pallete>(environment)
            processor.addListener(LogProcessorListener<Box, Pallete>())
            processor.addListener(ConsoleProcessorListener<Box, Pallete>(processor))
            processor.addListener(environment)

            val result = processor.process()

            log.info("Result: {}", result)
        }
        log.info("Finished. Time: $executionTime ms")
        /*
        println(normalizedPallet)
        println(palleteDimensions)
        println(palleteDimensions.center)
        println(palleteDimensions.distanceFromCenter.map { "%.3f".format(it) })
        println(palleteDimensions.points)
        println("positionsByRow   : ${palleteDimensions.positionsByRow}")
        println("positionsByColumn: ${palleteDimensions.positionsByColumn}")
        println(pallete.centerOfMass)
        println(pallete.momentOfInertia)
        println(pallete.totalMass)
        println(pallete.palleteToString)
        */


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

private fun getEnvironment(line: CommandLine): PalleteEnvironment {
    val (normalizedPallet, palleteDimensions) = getPallete(
            line.getOptionValue(WEIGHT_PARAMETER).trim().split(Regex(" +")).map { Integer.parseInt(it) },
            line.getOptionValue(ROWS_PARAMETER, "0").toInt(),
            line.getOptionValue(COLS_PARAMETER, "0").toInt())
    val pallete = Pallete(normalizedPallet.mapIndexed { index, weight -> Box(weight, index) }, palleteDimensions)

    return PalleteEnvironment(normalizedPallet, palleteDimensions, //
            maxGenerations = line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(),
            generationSize = line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "100").toInt())
}

