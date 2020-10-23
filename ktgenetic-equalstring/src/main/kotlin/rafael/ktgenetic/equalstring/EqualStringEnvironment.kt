package rafael.ktgenetic.equalstring

import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.createCutPositions
import kotlin.random.Random

class EqualStringEnvironment(val target: String,
                             val fitnessFunction: StringFitness,
                             override val maxGenerations: Int,
                             override val generationSize: Int,
                             override var mutationFactor: Double = 0.01
) : Environment<Char, Word> {

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private fun randomChar(): Char = range[Random.nextInt(range.size)]

    private fun createRandomWord(): Word =
            Word(1.rangeTo(target.length).map { randomChar() }.joinToString(""))

    override fun getFirstGeneration(): List<Word> =
            (1..generationSize).map { createRandomWord() }

    override fun executeMutation(sequence: List<Char>): List<Char> {
        val mutationPoint = Random.nextInt(sequence.size)
        val mutatedGene: Char = range[Random.nextInt(range.size)]
        val result = sequence.toMutableList()
        result[mutationPoint] = mutatedGene

        return result.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(target.length)

    override fun calculateFitness(chromosome: Word): Double =
            fitnessFunction.calculate(String(chromosome.content.toCharArray()), target)

    override fun resultFound(genotypes: List<Word>) =
            String(genotypes[0].content.toCharArray()) == target

    override fun createNewChromosome(sequence: List<Char>): Word = Word(sequence)

}
