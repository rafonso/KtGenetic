package rafael.ktgenetic

class OrderedGeneticProcessor<G, C : Chromosome<G>>(environment: Environment<G, C>) : GeneticProcessor<G, C>(environment) {

    private fun <G> executeCross(pieces1: Triple<List<G>, List<G>, List<G>>, pieces2: Triple<List<G>, List<G>, List<G>>): Pair<List<G>, List<G>> {

        fun cross(core: List<G>, parent: List<G>, firstCutPoint: Int): List<G> {
            val diff = parent - core
            val part1 = diff.subList(0, firstCutPoint)
            val part2 = diff.subList(firstCutPoint, diff.size)
            return part1 + core + part2
        }

        val child1 = cross(pieces1.second, pieces2.first + pieces2.second + pieces2.third, pieces2.first.size)
        val child2 = cross(pieces2.second, pieces1.first + pieces1.second + pieces1.third, pieces1.first.size)

        return Pair(child1, child2)
    }

    override fun <G> executeCrossing(pieces1: Triple<List<G>, List<G>, List<G>>, pieces2: Triple<List<G>, List<G>, List<G>>): Pair<List<G>, List<G>> {
        if (!pieces1.second.intersect(pieces2.first + pieces2.third).isEmpty() || !pieces2.second.intersect(pieces1.first + pieces1.third).isEmpty()) {
            return executeCross(pieces1, pieces2)
        }
        return super.executeCrossing(pieces1, pieces2)
    }

}