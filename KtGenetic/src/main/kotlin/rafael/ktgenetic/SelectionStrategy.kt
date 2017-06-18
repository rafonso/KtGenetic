package rafael.ktgenetic

interface SelectionStrategy<C: Chromosome<*>> {

    val generationSize: Int

    fun select(children: List<C>): List<C>

}