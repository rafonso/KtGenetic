package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Genotype
import rafael.ktgenetic.GenotypeFitnessComparator

data class Word(override val value: String) : Genotype<String>() {

    override fun valueToString(): String = "'$value'"

    override fun toString() = super.toString()

}

