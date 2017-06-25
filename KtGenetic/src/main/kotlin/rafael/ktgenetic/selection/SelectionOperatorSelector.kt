package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

fun <C : Chromosome<*>> chooseSelectionOperator(choice: String, generationSize: Int): SelectionOperator<C> {
    val operator: SelectionOperator<C>
    when (choice) {
        SelectionOperatorChoice.TRUNCATE.code -> operator = TruncateSelectionOperator<C>(generationSize)
        SelectionOperatorChoice.ROULETTE_ELITISM.code -> operator = RouletteElitismSelectionOperator<C>(generationSize)
        else -> throw IllegalArgumentException("Irregular Choice: $choice")
    }

    return operator
}
