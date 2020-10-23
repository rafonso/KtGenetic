package rafael.ktgenetic.core.utils

import kotlin.random.Random

/**
 * Create random positions
 *
 * @param maxPos
 * @param initialPos
 * @return
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
 * Generate a random [Int] from 0 to a max (exclusive)
 *
 * @param maxExclusive the max value
 * @return random [Int] from 0 until [maxExclusive] - 1
 */
fun randomIntExclusive(maxExclusive: Int) = Random.nextInt(maxExclusive)

/**
 * Generate a random [Int] from 0 to a max (inclusive)
 *
 * @param maxValue the max value
 * @return random [Int] from 0 until [maxValue]
 */
fun randomIntInclusive(maxValue: Int) = randomIntExclusive(maxValue + 1)

/**
 * Create cut positions
 *
 * @param maxPos
 * @return
 */
fun createCutPositions(maxPos: Int): Pair<Int, Int> = createRandomPositions(maxPos, 1)

