package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

fun <C : Chromosome<*>> chooseStrategy(choice: String, generationSize: Int): SelectionStrategy<C> {
    val strategy: SelectionStrategy<C>
    when (choice) {
        SelectionStrategyChoice.TRUNCATE.code -> strategy = TruncateFitnessSelectionStrategy<C>(generationSize)
        SelectionStrategyChoice.ROULETTE_ELITISM.code -> strategy = RouletteElitismSelectionStrategy<C>(generationSize)
        else -> throw IllegalArgumentException("Irregular Choice: $choice")
    }

    return strategy
}
