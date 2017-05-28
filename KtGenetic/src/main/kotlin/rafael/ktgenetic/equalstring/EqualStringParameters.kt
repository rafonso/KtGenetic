package rafael.ktgenetic.equalstring

import rafael.ktgenetic.GeneticParameters
import rafael.ktgenetic.Genotype
import java.util.*

class EqualStringParameters(val target: String,
                            override val generationSize: Int,
                            override val maxGenerations: Int,
                            override val mutationFactor: Double = 0.01) :
        GeneticParameters<String> {

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private val fitness: StringFitness = EqualCharsFitness()

    private fun randomChar(): Char = range[random.nextInt(range.size)]

    private fun createRandomWord(): String = 1.rangeTo(target.length).map { _ -> randomChar() }.joinToString("")

    override fun getFirstGeneration(): List<String> =
            1.rangeTo(generationSize)
                    .map { _ -> createRandomWord() }
                    .sorted()


    override fun executeMutation(segment: String): String {
        val bases = segment.toCharArray();
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

    override fun cutIntoPieces(gene: String, cutPositions: Pair<Int, Int>): Array<String> = arrayOf(
            gene.substring(0, cutPositions.first),
            gene.substring(cutPositions.first, cutPositions.second),
            gene.substring(cutPositions.second))

    override fun joinPieces(pieces: List<String>): String = pieces.joinToString("")

    override fun calculateFitness(genotype: String): Double = fitness.calculate(genotype, target)

    override fun resultFound(genotypes: List<Genotype<String>>) = genotypes[0].value.equals(target)

    override fun getNewGenetotype(genotype: String): Genotype<String> = Word(genotype)

}