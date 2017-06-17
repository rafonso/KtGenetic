package rafael.ktgenetic

import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private fun createRandomPositions(maxPos: Int, initialPos: Int): Pair<Int, Int> {
    val pos1 = initialPos + geneticRandom.nextInt(maxPos - 2)
    val pos2 = if (pos1 == (maxPos - 2))
        (maxPos - 1)
    else
        (pos1 + 1 + geneticRandom.nextInt(maxPos - 1 - pos1))

    return Pair(pos1, pos2)
}

fun <T> List<T>.shuffle(): List<T> {
    val temp = this.toMutableList()
    Collections.shuffle(temp)

    return temp
}

fun <T> List<T>.swap(index1: Int, index2: Int): List<T> {
    val copy = this.toMutableList()
    val temp = copy[index1]
    copy[index1] = copy[index2]
    copy[index2] = temp

    return copy.toList()
}

fun <T> List<T>.randomSwap(): List<T> {
    val positions = createCutPositions(this.size)
    return this.swap(positions.first, positions.second)
}

val geneticRandom = Random()

fun createCutPositions(maxPos: Int): Pair<Int, Int> = createRandomPositions(maxPos, 1)

fun <T> makeCuttingIntoPieces(sequence: List<T>, cutPositions: Pair<Int, Int>):
        Triple<List<T>, List<T>, List<T>> =
        Triple(
                sequence.subList(0, cutPositions.first),
                sequence.subList(cutPositions.first, cutPositions.second),
                sequence.subList(cutPositions.second, sequence.size)
        )

/**
 * https://stackoverflow.com/questions/34697828/parallel-operations-on-kotlin-collections
 *
 */
fun <T, R> Iterable<T>.pmap(
        numThreads: Int = Runtime.getRuntime().availableProcessors() - 2,
        exec: ExecutorService = Executors.newFixedThreadPool(numThreads),
        transform: (T) -> R): List<R> {

    // default size is just an inlined version of kotlin.collections.collectionSizeOrDefault
    val defaultSize = if (this is Collection<*>) this.size else 10
    val destination = Collections.synchronizedList(ArrayList<R>(defaultSize))

    for (item in this) {
        exec.submit { destination.add(transform(item)) }
    }

    exec.shutdown()
    exec.awaitTermination(1, TimeUnit.DAYS)

    return ArrayList<R>(destination)
}



