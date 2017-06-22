package rafael.ktgenetic.balancedtable

data class BalanceDimensions(val size: Int) {

    val center: Double = size.toDouble() / 2

    val half: Pair<List<Int>, List<Int>> by lazy {
        val pivot = size / 2 - 1
        val secondHalfBegin = if (size % 2 == 0) 0 else 1
        Pair((0 until pivot).toList(), (((pivot + secondHalfBegin) until size).toList()))
    }

    val blocks: List<Double> = (0 until size).map { it + 0.5 }

    val distanceFromCenter: List<Double> = blocks.map { Math.abs(it - center) }

}