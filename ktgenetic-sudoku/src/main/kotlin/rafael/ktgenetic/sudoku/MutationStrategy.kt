package rafael.ktgenetic.sudoku

import rafael.ktgenetic.core.utils.randomSwap
import kotlin.random.Random

enum class MutationStrategy {

    /**
     * Swap 2 random positions
     */
    SWAP {
        override fun getMutation(row: Row): Row = row.randomSwap()
    },

    /**
     * Shuffle the row
     */
    SHUFFLE {
        override fun getMutation(row: Row): Row = row.shuffled(Random)
    };

    abstract fun getMutation(row: Row): Row

}
