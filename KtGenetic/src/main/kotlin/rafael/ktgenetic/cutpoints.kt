import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val random = Random()
val MUTATION_FACTOR = 5
val log: Logger  = LoggerFactory.getLogger("cutpoints")

fun getCutPositions(size: Int): Pair<Int, Int> {
    val pos1 = 1 + random.nextInt(size - 2)
    val pos2 = 1 + random.nextInt(size - 1 - pos1) + pos1

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
		log.debug("MUTATION: Putting '${mutatedGene}' at position ${mutationPoint}  in ${segment}")
        bases[mutationPoint] = mutatedGene

        return String(bases)
    } else {
        return segment
    }
}


fun main(args: Array<String>) {
    val parent1 = args[0]
    var parent2 = args[1]

    log.debug(parent1 + " - " + parent2)

    if (parent1.length != parent2.length) {
        error("different sizes")
    }

    val cutPositions = getCutPositions(parent1.length)
    log.debug(cutPositions.toString())

    val cutString1 = cutString(parent1, cutPositions)
    val cutString2 = cutString(parent2, cutPositions)

    log.debug(cutString1.toList().toString())
    log.debug(cutString2.toList().toString())

    // Crossing
    val child1 = submitMutation(cutString2[0]) + cutString1[1] + submitMutation(cutString2[2])
    val child2 = submitMutation(cutString1[0]) + cutString2[1] + submitMutation(cutString1[2])

    log.debug(child1)
    log.debug(child2)
}