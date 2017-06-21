package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Chromosome

data class Word(override val content: List<Char>) : Chromosome<Char>() {

    constructor(strContent: String): this(strContent.toCharArray().toList())

    override fun valueToString(): String = "'" + String(content.toCharArray()) + "'"

    override fun toString() = super.toString()

}

