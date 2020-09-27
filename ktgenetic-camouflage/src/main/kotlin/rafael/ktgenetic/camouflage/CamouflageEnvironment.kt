package rafael.ktgenetic.camouflage

import rafael.ktgenetic.Environment
import rafael.ktgenetic.randomIntExclusive
import rafael.ktgenetic.randomIntInclusive
import rafael.ktgenetic.replace

class CamouflageEnvironment(
    initialBackgroundColor: Kolor,
    override val maxGenerations: Int,
    override val generationSize: Int,
    override var mutationFactor: Double = 0.01
) : Environment<Int, Kolor> {

    var backgroundColor = initialBackgroundColor

    private fun randomByte() = randomIntInclusive(MAX_COLOR_VALUE)

    private fun randomKolor(): Kolor = Kolor(randomByte(), randomByte(), randomByte())

    override fun getFirstGeneration(): List<Kolor> = (1..generationSize).map { randomKolor() }

    override fun getCutPositions(): Pair<Int, Int> {
        val pos = randomIntExclusive(3)
        return Pair(pos, pos + 1)
    }

    override fun executeMutation(sequence: List<Int>): List<Int> = sequence.replace(randomIntExclusive(3), randomByte())

    override fun createNewChromosome(sequence: List<Int>): Kolor = Kolor(sequence[0], sequence[1], sequence[2])

    override fun calculateFitness(chromosome: Kolor): Double = 1 / (1 + chromosome.distanceTo(this.backgroundColor))

}
