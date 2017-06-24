package rafael.ktgenetic.console

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.ProcessorEventEnum
import rafael.ktgenetic.ProcessorListener
import rafael.ktgenetic.processor.GeneticProcessor
import java.util.*

class ConsoleProcessorListener<G, C : Chromosome<G>>(val processor: GeneticProcessor<G, C>) : ProcessorListener {

    val scanner = Scanner(System.`in`)

    var generationsToWait: Int = 0

    var currentGeneration: Int = 0

    override fun onEvent(event: ProcessorEvent) {
        if (event.event == ProcessorEventEnum.GENERATION_EVALUATED) {
            currentGeneration++
            if (currentGeneration >= generationsToWait) {
                println("Type <Enter> to process the next generation. " +
                        "Type n to process 'n' generations. " +
                        "Type 0 to process until the end. " +
                        "Type x to interrupt the program")
                val input = scanner.nextLine()
                if(input.matches(Regex("\\d+"))) {
                    val x = Integer.parseInt(input)
                    if(x == 0) {
                        generationsToWait = Int.MAX_VALUE
                    } else {
                        generationsToWait = x
                        currentGeneration = 0
                    }
                } else if(input == "x") {
                    processor.stop()
                }
            }
        }
    }
}