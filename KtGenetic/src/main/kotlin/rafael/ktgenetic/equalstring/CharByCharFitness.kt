package rafael.ktgenetic.equalstring

abstract class CharByCharFitness : StringFitness {

    abstract fun calculateDiff(ch1: Char, ch2: Char): Int;

    override fun calculate(str1: String, str2: String): Double {
        if (str1.length != str1.length) {
            error("tamanhos diferentes")
        }

        var diff = 0
        for (i in 0 until str1.length) {
            diff += calculateDiff(str1[i], str2[i])
        }

        return Math.abs(1.0 / (1.0 + diff))
    }
}