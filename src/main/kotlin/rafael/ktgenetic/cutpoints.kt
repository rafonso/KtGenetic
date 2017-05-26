import java.util.*

val random = Random()
val MUTATION_FACTOR = 5

fun getCutPositions(size: Int): Pair<Int, Int> {
    val pos1 = 1 + random.nextInt(size - 2)
    val pos2 = 1 + random.nextInt(size - 2 - pos1) + pos1

    return Pair(pos1, pos2)
}

fun cutString(str: String, cutPositions: Pair<Int, Int>): Array<String> = arrayOf(
        str.substring(0, cutPositions.first),
        str.substring(cutPositions.first, cutPositions.second),
        str.substring(cutPositions.second))

fun submitMutation(segment: String): String {
    if (random.nextInt(MUTATION_FACTOR) == 0) {
        // Select position to be changed
        val bases = segment.toCharArray();
        val mutationPoint = random.nextInt(bases.size)
        val mutatedGene: Char = 'a' + random.nextInt('z' - 'a')
        println("MUTATION: Putting '${mutatedGene}' at position ${mutationPoint}  in ${segment}")
        bases[mutationPoint] = mutatedGene

        return String(bases)
    } else {
        return segment
    }
}


fun main(args: Array<String>) {
    val parent1 = args[0]
    var parent2 = args[1]

    println(parent1 + " - " + parent2)

    if (parent1.length != parent2.length) {
        error("different sizes")
    }

    val cutPositions = getCutPositions(parent1.length)
    println(cutPositions)

    val cutString1 = cutString(parent1, cutPositions)
    val cutString2 = cutString(parent2, cutPositions)

    println(cutString1.toList())
    println(cutString2.toList())

    // Crossing
    val child1 = submitMutation(cutString2[0]) + cutString1[1] + submitMutation(cutString2[2])
    val child2 = submitMutation(cutString1[0]) + cutString2[1] + submitMutation(cutString1[2])

    println(child1)
    println(child2)
}