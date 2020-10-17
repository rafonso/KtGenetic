package rafael.ktgenetic.equalstring

import rafael.ktgenetic.core.Chromosome

data class Word(override val content: List<Char>) : Chromosome<Char>() {

    constructor(strContent: String): this(strContent.toCharArray().toList())

    override fun valueToString(): String = "'" + String(content.toCharArray()) + "'"

    override fun toString() = super.toString()

    override fun compareTo(other: Chromosome<Char>): Int {
        val compare = super.compareTo(other)

        return if(compare == 0) String(this.content.toCharArray()).compareTo(String(other.content.toCharArray())) else compare
    }

}

