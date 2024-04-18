package rafael.ktgenetic.core.utils

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext

/**
 * Main logger for the application.
 */
val mainLogger = LogManager.getLogger("Main")!!


/**
 * Custom log level named "TRACER".
 */
val TRACER: Level = Level.forName("TRACER", 700)

/**
 * Enum representing the different log levels used in the application.
 *
 * @property level The Log4J [Level] object associated with the log level.
 * @property code The string code associated with the log level.
 */
enum class LogLevel(val level: Level, val code: String) {
    INFO(Level.INFO, 0.toString()),
    DEBUG(Level.DEBUG, 1.toString()),
    TRACE(Level.TRACE, 2.toString()),
    TRACER_LEVEL(TRACER, 3.toString())
}

/**
 * Converts a string code to a [LogLevel].
 *
 * @param code The string code to convert.
 * @return The LogLevel associated with the code.
 */
fun codeToLogLevel(code: String) = LogLevel.entries.first { it.code == code }

/**
 * Configures the application's log level.
 *
 * This function changes the log level of the application's root logger to the specified level.
 *
 * @param logLevel The LogLevel to set.
 */
fun configureLog(logLevel: LogLevel) {
    /**
     * Code extracted from https://stackoverflow.com/questions/23434252/programmatically-change-log-level-in-log4j2
     */
    val ctx = LogManager.getContext(false) as LoggerContext
    val config = ctx.configuration
    val loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
    loggerConfig.level = logLevel.level
    ctx.updateLoggers()  // This causes all Loggers to refetch information from their LoggerConfig.
}

/**
 * Configures the application's log level using a string code.
 *
 * This function converts the code to a LogLevel and then calls the other configureLog function.
 *
 * @param code The string code of the LogLevel to set.
 */
fun configureLog(code: String)  = configureLog(codeToLogLevel(code))
