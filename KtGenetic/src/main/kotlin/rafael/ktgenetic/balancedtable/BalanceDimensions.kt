package rafael.ktgenetic.balancedtable

data class BalanceDimensions(val size: Int) {

    val center: Double = size.toDouble() / 2

    val blocks: List<Double> = (0 until size).map { it + 0.5 }

    val distanceFromCenter: List<Double> = blocks.map { Math.abs(it - center) }

}