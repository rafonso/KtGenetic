package rafael.ktgenetic.selection

enum class SelectionStrategyChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation"),
    ROULETTE_ELITISM("r", "Roulette with Elitism")
}