package rafael.ktgenetic.core.utils

import kotlin.random.Random

/**
 * Creates a pair of random positions within a given range.
 *
 * @param maxPos The maximum possible position (exclusive).
 * @param initialPos The minimum possible position (default is 0).
 * @return A pair of random positions within the range [initialPos, maxPos).
 */
internal fun createRandomPositions(maxPos: Int, initialPos: Int = 0): Pair<Int, Int> {
    val pos1 = initialPos + Random.nextInt(maxPos - 2)
    val pos2 = if (pos1 == (maxPos - 2))
        (maxPos - 1)
    else
        (pos1 + 1 + Random.nextInt(maxPos - 1 - pos1))

    return Pair(pos1, pos2)
}

/**
 * Generates a random integer from 0 (inclusive) to a given maximum value (exclusive).
 *
 * @param maxExclusive The maximum value (exclusive).
 * @return A random integer within the range [0, maxExclusive).
 */
fun randomIntExclusive(maxExclusive: Int) = Random.nextInt(maxExclusive)

/**
 * Generates a random integer from 0 (inclusive) to a given maximum value (inclusive).
 *
 * @param maxValue The maximum value (inclusive).
 * @return A random integer within the range [0, maxValue].
 */
fun randomIntInclusive(maxValue: Int) = randomIntExclusive(maxValue + 1)

/**
 * Creates a pair of cut positions within a given range.
 *
 * The range is from 1 (inclusive) to the given maximum position (exclusive).
 *
 * @param maxPos The maximum possible position (exclusive).
 * @return A pair of cut positions within the range [1, maxPos).
 */
fun createCutPositions(maxPos: Int): Pair<Int, Int> = createRandomPositions(maxPos, 1)

