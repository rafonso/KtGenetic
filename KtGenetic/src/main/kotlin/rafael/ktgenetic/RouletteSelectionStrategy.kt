package rafael.ktgenetic

class RouletteSelectionStrategy<C : Chromosome<*>>(override val generationSize: Int) : SelectionStrategy<C> {

    tailrec fun selectPosition(children: List<C>, sortedValue: Double, pos: Int = 0): Int {
        if (sortedValue <= 0.0) {
            return pos
        }
        if (pos >= children.size) {
            return children.size - 1
        }
        return selectPosition(children, sortedValue - children[pos].fitness, pos + 1)
    }

    fun selectElement(children: List<C>, totalFitness: Double): C {
        val sortedValue = geneticRandom.nextDouble() * totalFitness
        val selectedPosition = selectPosition(children, sortedValue)
        val selectedChildren = children[selectedPosition]

        return selectedChildren
    }

    override fun select(children: List<C>): List<C> {
        val totalFitness = children.pmap { it.fitness }.sum()

        return (0 until generationSize).pmap { selectElement(children, totalFitness) }
    }

}