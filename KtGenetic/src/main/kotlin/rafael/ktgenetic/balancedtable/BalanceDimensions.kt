package rafael.ktgenetic.balancedtable

data class BalanceDimensions(val size: Int) {

    val center: Double = size.toDouble() / 2

    val positions: List<Double> = (0 until size).map { it + 0.5 }

    val distanceFromCenter: List<Double> = positions.map { Math.abs(it - center) }

}