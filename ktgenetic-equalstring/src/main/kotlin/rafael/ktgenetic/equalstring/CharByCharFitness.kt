package rafael.ktgenetic.equalstring

import kotlin.math.abs

abstract class CharByCharFitness : StringFitness {

    abstract fun calculateDiff(ch1: Char, ch2: Char): Int

    override fun calculate(str1: String, str2: String): Double {
        if (str1.length != str2.length) {
            error("Different sizes: '$str1' x '$str2'")
        }

        val diff = (str1.indices).sumBy { calculateDiff(str1[it], str2[it]) }

        return abs(1.0 / (1.0 + diff))
    }
}