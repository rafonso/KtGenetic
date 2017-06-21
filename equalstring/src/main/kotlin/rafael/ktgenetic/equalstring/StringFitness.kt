package rafael.ktgenetic.equalstring

interface StringFitness {
    fun calculate(str1: String, str2 : String): Double
}