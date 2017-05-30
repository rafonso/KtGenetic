package rafael.ktgenetic.equalstring

import rafael.ktgenetic.Environment
import java.util.*

class EqualStringEnvironment(val target: String,
                             override val generationSize: Int,
                             override val maxGenerations: Int,
                             override val mutationFactor: Double = 0.01) :
        Environment<String, Word> {

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private val fitness: StringFitness = EqualCharsFitness()

    private fun randomChar(): Char = range[random.nextInt(range.size)]

    private fun createRandomWord(): Word = Word(1.rangeTo(target.length).map { _ -> randomChar() }.joinToString(""))

    override fun getFirstGeneration(): List<Word> =
            1.rangeTo(generationSize)
                    .map { _ -> createRandomWord() }
//                   .sorted({(w1, w2) -> w1.value.compareTo(w2.value)})


    override fun executeMutation(gene: String): String {
        val bases = gene.toCharArray();
        val mutationPoint = random.nextInt(bases.size)
        val mutatedGene: Char = range[random.nextInt(range.size)]
        bases[mutationPoint] = mutatedGene

        return String(bases)
    }

    override fun getCutPositions(): Pair<Int, Int> {
        val pos1 = 1 + random.nextInt(target.length - 2)
        val pos2 = 1 + random.nextInt(target.length - 1 - pos1) + pos1

        return Pair(pos1, pos2)
    }

    override fun cutIntoPieces(gene: String, cutPositions: Pair<Int, Int>): Triple<String, String, String> = Triple(
            gene.substring(0, cutPositions.first),
            gene.substring(cutPositions.first, cutPositions.second),
            gene.substring(cutPositions.second))

    override fun joinPieces(segments: List<String>): String = segments.joinToString("")

    override fun calculateFitness(gene: String): Double = fitness.calculate(gene, target)

    override fun resultFound(genotypes: List<Word>) = genotypes[0].value.equals(target)

    override fun getNewGenetotype(gene: String): Word = Word(gene)

}