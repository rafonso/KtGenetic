package rafael.ktgenetic

data class Word(val value: String, var fitness: Double = 0.0) {
	override fun toString() = "['$value', ${"%.3f".format(fitness)}]"
}

class WordValueComparator: Comparator<Word> {
	override fun compare(w1: Word, w2: Word): Int = w1.value.compareTo(w2.value)
}

class WordFitnessComparator: Comparator<Word> {
	override fun compare(w1: Word, w2: Word): Int = w1.fitness.compareTo(w2.fitness)
}

