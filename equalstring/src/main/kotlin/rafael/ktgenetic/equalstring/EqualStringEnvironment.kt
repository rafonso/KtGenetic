package rafael.ktgenetic.equalstring

class EqualStringEnvironment(val target: String,
                             val fitnessFunction: rafael.ktgenetic.equalstring.StringFitness,
                             override val maxGenerations: Int,
                             override val generationSize: Int,
                             override var mutationFactor: Double = 0.01
) : rafael.ktgenetic.Environment<Char, Word> {

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private fun randomChar(): Char = range[rafael.ktgenetic.geneticRandom.nextInt(range.size)]

    private fun createRandomWord(): rafael.ktgenetic.equalstring.Word =
            rafael.ktgenetic.equalstring.Word(1.rangeTo(target.length).map { _ -> randomChar() }.joinToString(""))

    override fun getFirstGeneration(): List<rafael.ktgenetic.equalstring.Word> =
            (1..generationSize).map { _ -> createRandomWord() }

    override fun executeMutation(sequence: List<Char>): List<Char> {
        val mutationPoint = rafael.ktgenetic.geneticRandom.nextInt(sequence.size)
        val mutatedGene: Char = range[rafael.ktgenetic.geneticRandom.nextInt(range.size)]
        val result = sequence.toMutableList()
        result[mutationPoint] = mutatedGene

        return result.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = rafael.ktgenetic.createCutPositions(target.length)

    override fun calculateFitness(sequence: List<Char>): Double =
            fitnessFunction.calculate(String(sequence.toCharArray()), target)

    override fun resultFound(genotypes: List<rafael.ktgenetic.equalstring.Word>) =
            String(genotypes[0].content.toCharArray()) == target

    override fun getNewGenotype(sequence: List<Char>): rafael.ktgenetic.equalstring.Word = rafael.ktgenetic.equalstring.Word(sequence)

}