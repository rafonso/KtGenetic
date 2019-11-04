package rafael.ktgenetic.sudoku

import rafael.ktgenetic.geneticRandom
import rafael.ktgenetic.randomSwap

enum class MutationStrategy(val description: String) {

    SWAP("Swap 2 random positions") {
        override fun getMutation(row: Row): Row = row.randomSwap()
    },
    SHUFFLE("Shuffle the row") {
        override fun getMutation(row: Row): Row = row.shuffled(geneticRandom)
    };

    abstract fun getMutation(row: Row): Row

}