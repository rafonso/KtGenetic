package rafael.ktgenetic.equalstring

import kotlin.math.abs

class SubtractCharsFitness: CharByCharFitness() {
    override fun calculateDiff(str1: String, str2: String): Double {
        val diff = str1.indices.sumBy { abs(str1[it] - str2[it]) }

        return abs(1.0 / (1.0 + diff))
    }
}
