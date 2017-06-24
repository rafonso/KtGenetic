package rafael.ktgenetic

import org.apache.logging.log4j.Level
import java.util.*

private fun createRandomPositions(maxPos: Int, initialPos: Int): Pair<Int, Int> {
    val pos1 = initialPos + geneticRandom.nextInt(maxPos - 2)
    val pos2 = if (pos1 == (maxPos - 2))
        (maxPos - 1)
    else
        (pos1 + 1 + geneticRandom.nextInt(maxPos - 1 - pos1))

    return Pair(pos1, pos2)
}

val TRACER: Level = Level.forName("TRACER", 700)

val geneticRandom = Random()

fun createCutPositions(maxPos: Int): Pair<Int, Int> = createRandomPositions(maxPos, 1)

fun <G> makeCuttingIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>):
        ListPieces<G> =
        ListPieces(
                sequence.subList(0, cutPositions.first),
                sequence.subList(cutPositions.first, cutPositions.second),
                sequence.subList(cutPositions.second, sequence.size)
        )
