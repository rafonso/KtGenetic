package rafael.ktgenetic

data class Word(override val value: String) : Genotype<String>() {

    override fun valueToString(): String = "'$value'"

    override fun toString() = super.toString()

}

class WordFitnessComparator : GenotypeFitnessComparator<String>() {}

