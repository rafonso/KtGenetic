package rafael.ktgenetic

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class GeneticProcessor(val parameter: ProcessorParameters) {

    val log: Logger = LoggerFactory.getLogger("GeneticProcessor")

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private fun randomChar(): Char = range[random.nextInt(range.size)]

    private fun createRandomWord(length: Int): String = 1.rangeTo(length).map { _ -> randomChar() }.joinToString("")

    private fun terminate(population: List<Word>, target: String): Boolean = population.any { it -> it.value.equals(target) }


    private fun createFirstGeneration(target: String) = 1.rangeTo(parameter.childrenToSave)
            .map { _ -> createRandomWord(target.length) }
            .sorted().map { str -> Word(str) }

    public fun process(target: String): String {
        var population = createFirstGeneration(target)

        log.info(target)
        log.debug(population.toString())

        var generation = 1
        while (true && (generation <= parameter.maxGenerations)) { //}terminate(population, target)) {

            log.trace(generation.toString())
            generation++
        }

        return ""
    }


}