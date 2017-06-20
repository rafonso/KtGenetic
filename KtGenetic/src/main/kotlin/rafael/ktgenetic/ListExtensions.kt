/**
 * Extension Methods for List
 */
package rafael.ktgenetic

import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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
    if (this.isEmpty()) return listOf()
    if (this.size == 1) return listOf(this[0])
    if (this.size == 2) return listOf(this[1], this[0])

    val positions = createCutPositions(this.size)
    return this.swap(positions.first, positions.second)
}

/**
 * https://stackoverflow.com/questions/34697828/parallel-operations-on-kotlin-collections
 *
 */
fun <T, R> Iterable<T>.pMap(
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

    return destination.toList()
}

/**
 * https://stackoverflow.com/questions/34697828/parallel-operations-on-kotlin-collections
 *
 */
fun <T, R> Iterable<T>.pFlatMap(
        numThreads: Int = Runtime.getRuntime().availableProcessors() - 2,
        exec: ExecutorService = Executors.newFixedThreadPool(numThreads),
        transform: (T) -> List<R>): List<R> {

    // default size is just an inlined version of kotlin.collections.collectionSizeOrDefault
    val defaultSize = if (this is Collection<*>) this.size else 10
    val destination = Collections.synchronizedList(ArrayList<R>(defaultSize))

    for (item in this) {
        exec.submit { destination.addAll(transform(item)) }
    }

    exec.shutdown()
    exec.awaitTermination(1, TimeUnit.DAYS)

    return destination.toList()
}
