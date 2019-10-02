/**
 * Extension Methods for List
 */
package rafael.ktgenetic

import java.util.*
import java.util.stream.Collectors

fun <T> List<T>.shuffle(): List<T> {
    val temp = this.toMutableList()
    temp.shuffle()

    return temp
}

/**
 * Swaps the values
 */
fun <T> List<T>.swap(index1: Int, index2: Int): List<T> {
    if(index1 == index2) return this

    val copy = this.toMutableList()
    val temp = copy[index1]
    copy[index1] = copy[index2]
    copy[index2] = temp

    return copy.toList()
}

fun <T> List<T>.randomSwap(): List<T> {
    if (this.isEmpty()) return this
    if (this.size == 1) return this
    if (this.size == 2) return listOf(this[1], this[0])

    val positions = createCutPositions(this.size)
    return this.swap(positions.first, positions.second)
}

fun <T, R> Iterable<T>.pMap(transform: (T) -> R): List<R> =
        ArrayList(toMutableList()).
                parallelStream().
                map { transform(it) }.
                collect(Collectors.toList()).
                toList()


fun <T, R> Iterable<T>.pFlatMap(transform: (T) -> List<R>): List<R> =
        ArrayList(toMutableList()).
                parallelStream().
                flatMap { ArrayList(transform(it)).stream() }.
                collect(Collectors.toList()).
                toList()
