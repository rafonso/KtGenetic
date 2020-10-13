package rafael.ktgenetic

/**
 * Creates a new [ListPieces]
 *
 * @param sequence List to be cut
 * @param cutPositions cutting positions, where 0 <= [Pair.first] <= [Pair.second] <= [sequence].[List.size]
 * @param G [sequence] type
 * @return a new [ListPieces]
 * @throws IllegalArgumentException if [cutPositions] does not follow the specification above.
 */
fun <G> makeCuttingIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>):
        ListPieces<G> {
    require((cutPositions.first >= 0) && (cutPositions.first <= cutPositions.second) && (cutPositions.second <= sequence.size)) {
        "Cut positions crescent numbers must be between 0 and sequence size (${sequence.size}): $cutPositions"
    }

    // @formatter:off
    return ListPieces(
        sequence.subList(0                  , cutPositions.first    ),
        sequence.subList(cutPositions.first , cutPositions.second   ),
        sequence.subList(cutPositions.second, sequence.size         )
    )
    // @formatter:on
}

data class ListPieces<out G>(val left: List<G>, val core: List<G>, val right: List<G>) {

    val join: List<G> by lazy {
        left + core + right
    }

}
