package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Chromosome

data class Word(override val value: String) : Chromosome<String>() {

    override fun valueToString(): String = "'$value'"

    override fun toString() = super.toString()

}

