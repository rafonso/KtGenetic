package rafael.ktgenetic.equalstring

import kotlin.math.abs

class SubtractCharsFitness: CharByCharFitness() {
    override fun calculateDiff(ch1: Char, ch2: Char ): Int = abs(ch1 - ch2)
}