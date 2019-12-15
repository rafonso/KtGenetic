package rafael.ktgenetic.equalstring

class EqualCharsFitness : CharByCharFitness() {
    override fun calculateDiff(str1: String, str2: String): Double {
        val diff = str1.indices.count { str1[it] != str2[it] }.toDouble()

        return (str1.length - diff) / str1.length
    }
}
