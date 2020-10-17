package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.Chromosome

interface SelectionOperator<C : Chromosome<*>> {

    val generationSize: Int

    // TODO: Transformar em not null
    val allowRepetition: Boolean?
        get() = false

    fun select(children: List<C>): List<C>

}

internal fun selectorToString(selector: SelectionOperator<*>) =
        "${selector.javaClass.simpleName}(${selector.generationSize}, ${selector.allowRepetition})"
