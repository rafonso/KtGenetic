package rafael.ktgenetic.core.utils

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext

val mainLogger = LogManager.getLogger("Main")!!

val TRACER: Level = Level.forName("TRACER", 700)

enum class LogLevel(val level: Level, val code: String) {
    INFO(Level.INFO, 0.toString()),
    DEBUG(Level.DEBUG, 1.toString()),
    TRACE(Level.TRACE, 2.toString()),
    TRACER_LEVEL(TRACER, 3.toString())
}

fun codeToLogLevel(code: String) = LogLevel.entries.first { it.code == code }

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

fun configureLog(code: String)  = configureLog(codeToLogLevel(code))
