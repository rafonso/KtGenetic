package rafael.ktgenetic.selection

enum class SelectionOperatorChoice(val code: String, val description: String) {
    TRUNCATE("t", "Truncation"),
    ROULETTE_ELITISM("r", "Roulette with Elitism")
}