package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

interface SelectionOperator<C: Chromosome<*>> {

    val generationSize: Int

    fun select(children: List<C>): List<C>

}