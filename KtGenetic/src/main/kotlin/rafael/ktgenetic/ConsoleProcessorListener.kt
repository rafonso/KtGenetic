package rafael.ktgenetic

import java.util.*

/**
 * Created by rucaf_000 on 27/05/2017.
 */
class ConsoleProcessorListener : ProcessorListener {

    val scanner = Scanner(System.`in`)

    var generationsToWait: Int = 0

    var currentGeneration: Int = 0;

    override fun onEvent(event: ProcessorEvent) {
        if (event.event == ProcessorEventEnum.GENERATION_EVALUATED) {
            currentGeneration++;
            if (currentGeneration >= generationsToWait) {
                println("Type <Enter> to process the next generation. " +
                        "Type n to process 'n' generations. " +
                        "Type 0 to process until the end.")
                val input = scanner.nextLine()
                if(input.matches(Regex("\\d+"))) {
                    val x = Integer.parseInt(input)
                    if(x == 0) {
                        generationsToWait = Int.MAX_VALUE
                    } else {
                        generationsToWait = x
                        currentGeneration = 0
                    }
                }
            }
        }
    }
}