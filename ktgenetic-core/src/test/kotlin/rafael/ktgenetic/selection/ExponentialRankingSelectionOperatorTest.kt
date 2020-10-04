package rafael.ktgenetic.selection

class ExponentialRankingSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        ExponentialRankingSelectionOperator(generationSize, allowRepetition)

}
