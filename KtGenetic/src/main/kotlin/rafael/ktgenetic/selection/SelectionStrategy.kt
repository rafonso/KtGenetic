package rafael.ktgenetic.selection

interface SelectionStrategy<C: rafael.ktgenetic.Chromosome<*>> {

    val generationSize: Int

    fun select(children: List<C>): List<C>

}