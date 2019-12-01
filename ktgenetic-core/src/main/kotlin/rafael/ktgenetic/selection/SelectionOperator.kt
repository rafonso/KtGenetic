package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

interface SelectionOperator<C : Chromosome<*>> {

    val generationSize: Int
    
    val allowRepetition: Boolean
        get() = false

    fun select(children: List<C>): List<C>

}

internal fun selectorToString(selector: SelectionOperator<*>) =
        "${selector.javaClass.simpleName}(${selector.generationSize}, ${selector.allowRepetition})"