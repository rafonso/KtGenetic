package rafael.ktgenetic

import java.util.*

class Breeder(val mutationFactor: Double = 0.01) {

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private fun getCutPositions(size: Int): Pair<Int, Int> {
        val pos1 = 1 + random.nextInt(size - 2)
        val pos2 = 1 + random.nextInt(size - 1 - pos1) + pos1

        return Pair(pos1, pos2)
    }

    private fun cutString(str: String, cutPositions: Pair<Int, Int>): Array<String> = arrayOf(
            str.substring(0, cutPositions.first),
            str.substring(cutPositions.first, cutPositions.second),
            str.substring(cutPositions.second))

    private fun submitMutation(segment: String): String {
        if (random.nextDouble() < mutationFactor) {
            // Select position to be changed
            val bases = segment.toCharArray();
            val mutationPoint = random.nextInt(bases.size)
            val mutatedGene: Char = range[random.nextInt(range.size)]
            bases[mutationPoint] = mutatedGene

            return String(bases)
        } else {
            return segment
        }
    }

    public fun cross(parent1: String, parent2: String): List<String> {
        val cutPositions = getCutPositions(parent1.length)

        val cutString1 = cutString(parent1, cutPositions)
        val cutString2 = cutString(parent2, cutPositions)

        // Crossing
        val child1 = submitMutation(cutString2[0]) + cutString1[1] + submitMutation(cutString2[2])
        val child2 = submitMutation(cutString1[0]) + cutString2[1] + submitMutation(cutString1[2])

        return listOf(child1, child2)
    }

}