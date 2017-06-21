package rafael.ktgenetic.equalstring

abstract class CharByCharFitness : rafael.ktgenetic.equalstring.StringFitness {

    abstract fun calculateDiff(ch1: Char, ch2: Char): Int

    override fun calculate(str1: String, str2: String): Double {
        if (str1.length != str2.length) {
            error("Different sizes: '$str1' x '$str2'")
        }

        val diff = (0 until str1.length).sumBy { calculateDiff(str1[it], str2[it]) }

        return Math.abs(1.0 / (1.0 + diff))
    }
}