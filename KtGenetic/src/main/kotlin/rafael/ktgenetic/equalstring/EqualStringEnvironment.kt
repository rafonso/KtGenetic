package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.geneticRandom
import rafael.ktgenetic.makeCuttingIntoPieces

class EqualStringEnvironment(val target: String,
                             override val generationSize: Int,
                             override val maxGenerations: Int,
                             override val mutationFactor: Double = 0.01) :
        Environment<Char, Word> {

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private val fitness: StringFitness = EqualCharsFitness()

    private fun randomChar(): Char = range[geneticRandom.nextInt(range.size)]

    private fun createRandomWord(): Word = Word(1.rangeTo(target.length).map { _ -> randomChar() }.joinToString(""))

    override fun getFirstGeneration(): List<Word> =
            1.rangeTo(generationSize)
                    .map { _ -> createRandomWord() }

    override fun executeMutation(sequence: List<Char>): List<Char> {
        val mutationPoint = geneticRandom.nextInt(sequence.size)
        val mutatedGene: Char = range[geneticRandom.nextInt(range.size)]
        val result = sequence.toMutableList()
        result[mutationPoint] = mutatedGene

        return result.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(target.length)

    override fun cutIntoPieces(sequence: List<Char>, cutPositions: Pair<Int, Int>): Triple<List<Char>, List<Char>, List<Char>> =
            makeCuttingIntoPieces(sequence, cutPositions)

    override fun calculateFitness(sequence: List<Char>): Double = fitness.calculate(String(sequence.toCharArray()), target)

    override fun resultFound(genotypes: List<Word>) = String(genotypes[0].content.toCharArray()).equals(target)

    override fun getNewGenotype(sequence: List<Char>): Word = Word(sequence)

}