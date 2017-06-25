package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.geneticRandom
import rafael.ktgenetic.pMap

class EqualStringEnvironment(val target: String,
                             val fitnessFunction: StringFitness,
                             override val maxGenerations: Int,
                             override val generationSize: Int,
                             override var mutationFactor: Double = 0.01
) : Environment<Char, Word> {

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private fun randomChar(): Char = range[rafael.ktgenetic.geneticRandom.nextInt(range.size)]

    private fun createRandomWord(): Word =
            Word(1.rangeTo(target.length).pMap { _ -> randomChar() }.joinToString(""))

    override fun getFirstGeneration(): List<Word> =
            (1..generationSize).map { _ -> createRandomWord() }

    override fun executeMutation(sequence: List<Char>): List<Char> {
        val mutationPoint = geneticRandom.nextInt(sequence.size)
        val mutatedGene: Char = range[geneticRandom.nextInt(range.size)]
        val result = sequence.toMutableList()
        result[mutationPoint] = mutatedGene

        return result.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(target.length)

    override fun calculateFitness(sequence: List<Char>): Double =
            fitnessFunction.calculate(String(sequence.toCharArray()), target)

    override fun resultFound(genotypes: List<Word>) =
            String(genotypes[0].content.toCharArray()) == target

    override fun createNewChromosome(sequence: List<Char>): Word = Word(sequence)

}