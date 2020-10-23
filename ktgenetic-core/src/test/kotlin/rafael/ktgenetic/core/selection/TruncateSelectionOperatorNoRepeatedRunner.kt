package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.TemplateChromosome

const val CYCLES = 10_000

val sizes = arrayOf(1, 5, 10, 50, 100, 500, 1_000, 5_000, 10_000, 50_000, 100_000)

fun heating(chromossomes: List<Chromosome<*>>) {
    println("Heating ...")
    val operator = TruncateSelectionOperator<Chromosome<*>>(10, false)
    repeat(500) {
        operator.select(chromossomes)
    }
}


fun runOperator(size: Int, chromossomes: List<Chromosome<*>>): Long {
    val operator = TruncateSelectionOperator<Chromosome<*>>(size, false)

    val t0 = System.currentTimeMillis()
    repeat(CYCLES) {
        operator.select(chromossomes)
    }
    return System.currentTimeMillis() - t0
}

fun main() {
    val chromossomes = TemplateChromosome.createRamdomChromossomes(1000)

    heating(chromossomes)

    sizes.forEach { size ->
        val deltaT = runOperator(size, chromossomes)
        println("%6d\t%4d".format(size, deltaT))
    }
}
