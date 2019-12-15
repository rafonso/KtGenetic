package rafael.ktgenetic.equalstring

abstract class CharByCharFitness : StringFitness {

    abstract fun calculateDiff(str1: String, str2: String): Double

    override fun calculate(str1: String, str2: String): Double {
        check(str1.length == str2.length) {
            "Different sizes: '$str1' x '$str2'"
        }

        return calculateDiff(str1, str2)
    }
}
