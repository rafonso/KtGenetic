package rafael.ktgenetic.equalstring

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import rafael.ktgenetic.CHILDREN_TO_SURVIVE_PARAMETER
import rafael.ktgenetic.GENERATIONS_PARAMETER
import rafael.ktgenetic.executeMain

private const val WORD_PARAMETER = "w"

private fun addOptions(options: Options) {
    options.addOption(WORD_PARAMETER, true, "Word")
}

private fun validateParameters(line: CommandLine) {
    if (!line.hasOption(WORD_PARAMETER)) {
        throw IllegalArgumentException("Please provide the word to be processed")
    }
}

private fun getEnvironment(line: CommandLine) =
        EqualStringEnvironment(
                line.getOptionValue(WORD_PARAMETER), //
                line.getOptionValue(GENERATIONS_PARAMETER, "100").toInt(),
                line.getOptionValue(CHILDREN_TO_SURVIVE_PARAMETER, "10").toInt())

fun main(args: Array<String>) {
    executeMain(
            args,
            ::addOptions,
            ::validateParameters,
            ::getEnvironment
    )
}
