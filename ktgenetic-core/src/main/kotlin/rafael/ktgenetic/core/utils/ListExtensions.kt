/**
 * Extension Methods for List
 */
package rafael.ktgenetic.core.utils

import java.util.*
import java.util.stream.Collectors

/**
 * Returns a copy of this List with the requested position filled with the requested value.
 *
 * @receiver [List] of type [T]
 * @param T List type
 * @param index requested position
 * @param value requested value
 * @return A copy of this List with the requested position filled with the requested value.
 */
fun <T> List<T>.replace(index: Int, value: T): List<T> {
    val temp = this.toMutableList()
    temp[index] = value
    return temp
}

/**
 * Creates a copy of this list where the values of the requested indexes are swapped.
 *
 * @receiver [List] of type [T]
 * @param T List type
 * @param index1 First position in List
 * @param index2 Second position in List
 * @return A copy of this list where the values of the requested indexes are swapped. If both indexes are equals,
 * the list itself is returned.
 */
internal fun <T> List<T>.swap(index1: Int, index2: Int): List<T> {
    if (index1 == index2) return this

    val copy = this.toMutableList()
    val temp = copy[index1]
    copy[index1] = copy[index2]
    copy[index2] = temp

    return copy.toList()
}

/**
 * Creates a copy of this list where the values of 2 random indexes are swapped.
 *
 * @receiver [List] of type [T]
 * @param T List type
 * @return A copy of this list where the values of 2 random indexes are swapped. If it is empty or has
 * size 1, itself is returned. If it  has size 2
 */
fun <T> List<T>.randomSwap(): List<T> {
    if (this.isEmpty()) return this
    if (this.size == 1) return this
    if (this.size == 2) return this.asReversed()

    val positions = createCutPositions(this.size)
    return this.swap(positions.first, positions.second)
}

/**
 * Do the same as [Iterable.flatMap] but using Java's parallel streams.
 *
 * @receiver [Iterable] of type [T]
 * @param T [Iterable]´s type
 * @param R Type to be returned
 * @param transform Transformation function from [T] to [R]
 * @return List transformed from type [T] to [R]
 */
internal fun <T, R> Iterable<T>.pFlatMap(transform: (T) -> List<R>): List<R> =
    ArrayList(toMutableList()).parallelStream().flatMap { ArrayList(transform(it)).stream() }
        .collect(Collectors.toList()).toList()

/**
 * Do the same as [Iterable.forEach] but using Java's parallel streams.
 *
 * @param T [Iterable]´s type
 * @param action Action to be done for each element of iterable
 * @receiver [Iterable] of type [T]
 */
internal fun <T> Iterable<T>.pForEach(action: (T) -> Unit) {
    this.toList().parallelStream().forEach(action)
}
