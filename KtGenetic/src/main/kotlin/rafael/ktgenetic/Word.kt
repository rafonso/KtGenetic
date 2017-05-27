package rafael.ktgenetic

data class Word(val value: String, var fitness: Double = 0.0) : Comparable<Word> {

	override fun toString() = "['$value', ${"%.2f".format(fitness)}]"

	override fun compareTo(other: Word): Int {
		return ((this.fitness - other.fitness) * 1000).toInt()
	}
}