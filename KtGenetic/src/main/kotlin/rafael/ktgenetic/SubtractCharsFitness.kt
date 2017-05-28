package rafael.ktgenetic

class SubtractCharsFitness: CharByCharFitness() {
    override fun calculateDiff(ch1: Char, ch2: Char ): Int = Math.abs(ch1 - ch2)
}