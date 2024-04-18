package rafael.ktgenetic.core.utils

/**
 * Creates a new [ListPieces]
 *
 * This function takes a list and a pair of cut positions, and returns a new ListPieces object.
 * The ListPieces object contains three lists: the left, core, and right parts of the original list, cut at the specified positions.
 *
 * @param sequence List to be cut
 * @param cutPositions cutting positions, where 0 <= [Pair.first] <= [Pair.second] <= [sequence].[List.size]
 * @param G [sequence] type
 * @return a new [ListPieces]
 * @throws IllegalArgumentException if [cutPositions] does not follow the specification above.
 */
internal fun <G> makeCuttingIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>):
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

/**
 * Data class representing a list cut into three pieces.
 *
 * This class contains three lists: left, core, and right, which represent the three pieces of the original list.
 * The join property is a lazy property that concatenates the three lists back into a single list.
 *
 * @param G The type of the elements in the lists.
 */
data class ListPieces<out G>(val left: List<G>, val core: List<G>, val right: List<G>) {

    /**
     * The concatenated list of left, core, and right lists.
     *
     * This property is calculated lazily, i.e., it is calculated and cached the first time it is accessed.
     */
    val join: List<G> by lazy {
        left + core + right
    }

}
