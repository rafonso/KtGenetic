package rafael.ktgenetic.selection

class RouletteSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        RouletteSelectionOperator(generationSize, allowRepetition)

}
