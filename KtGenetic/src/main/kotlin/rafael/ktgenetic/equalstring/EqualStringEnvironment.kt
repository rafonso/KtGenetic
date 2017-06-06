package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Environment
import java.util.*

class EqualStringEnvironment(val target: String,
                             override val generationSize: Int,
                             override val maxGenerations: Int,
                             override val mutationFactor: Double = 0.01) :
        Environment<Char, Word> {

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private val fitness: StringFitness = EqualCharsFitness()

    private fun randomChar(): Char = range[random.nextInt(range.size)]

    private fun createRandomWord(): Word = Word(1.rangeTo(target.length).map { _ -> randomChar() }.joinToString(""))

    override fun getFirstGeneration(): List<Word> =
            1.rangeTo(generationSize)
                    .map { _ -> createRandomWord() }

    override fun executeMutation(sequence: List<Char>): List<Char> {
        val mutationPoint = random.nextInt(sequence.size)
        val mutatedGene: Char = range[random.nextInt(range.size)]
        val result = sequence.toMutableList()
        result[mutationPoint] = mutatedGene

        return result.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> {
        val pos1 = 1 + random.nextInt(target.length - 2)
        val pos2 = 1 + random.nextInt(target.length - 1 - pos1) + pos1

        return Pair(pos1, pos2)
    }

    override fun cutIntoPieces(sequence: List<Char>, cutPositions: Pair<Int, Int>): Triple<List<Char>, List<Char>, List<Char>> = Triple(
            sequence.subList(0, cutPositions.first),
            sequence.subList(cutPositions.first, cutPositions.second),
            sequence.subList(cutPositions.second, sequence.size))

    override fun calculateFitness(sequence: List<Char>): Double = fitness.calculate(String(sequence.toCharArray()), target)

    override fun resultFound(genotypes: List<Word>) = String(genotypes[0].content.toCharArray()).equals(target)

    override fun getNewGenetotype(sequence: List<Char>): Word = Word(sequence)

}