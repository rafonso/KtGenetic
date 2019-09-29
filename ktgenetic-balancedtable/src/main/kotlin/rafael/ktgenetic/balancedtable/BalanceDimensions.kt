package rafael.ktgenetic.balancedtable

import kotlin.math.abs

data class BalanceDimensions(val size: Int) {

    private fun calcHalves(): Pair<List<Int>, List<Int>>  {
        val pivot = size / 2
        val secondHalfBegin = if (size % 2 == 0) 0 else 1
        return Pair((0 until pivot).toList(), (((pivot + secondHalfBegin) until size).toList()))
    }

    val center: Double = size.toDouble() / 2

    val halves: Pair<List<Int>, List<Int>> = calcHalves()

    val blocks: List<Double> = (0 until size).map { it + 0.5 }

    val distanceFromCenter: List<Double> = blocks.map { abs(it - center) }

}