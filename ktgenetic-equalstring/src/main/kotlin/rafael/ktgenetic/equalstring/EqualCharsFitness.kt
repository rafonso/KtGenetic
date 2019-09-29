package rafael.ktgenetic.equalstring

class EqualCharsFitness : CharByCharFitness() {
    override fun calculateDiff(ch1: Char, ch2: Char ): Int = if(ch1 == ch2) 0 else 1
}