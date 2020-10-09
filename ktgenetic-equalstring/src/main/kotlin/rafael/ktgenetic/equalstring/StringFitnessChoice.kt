package rafael.ktgenetic.equalstring

enum class StringFitnessChoice(val code: String, val description: String, val fitness: StringFitness) {

    EQUAL_CHARS("e", "Equal Chars", EqualCharsFitness()),
    SUBSTRACT_CHARS("s", "Subtract Chars", SubtractCharsFitness()),

}
