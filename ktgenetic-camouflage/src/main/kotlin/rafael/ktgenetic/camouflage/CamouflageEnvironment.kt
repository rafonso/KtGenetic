package rafael.ktgenetic.camouflage

import javafx.beans.property.SimpleObjectProperty
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.randomIntExclusive
import rafael.ktgenetic.core.utils.randomIntInclusive
import rafael.ktgenetic.core.utils.replace
import tornadofx.getValue
import tornadofx.setValue

class CamouflageEnvironment(
    initialBackgroundColor: Kolor,
    private val distanceCalulator: KolorDistance,
    override val maxGenerations: Int,
    override val generationSize: Int,
    override var mutationFactor: Double = 0.01
) : Environment<Int, Kolor> {

    private val backgroundColorProperty = SimpleObjectProperty(initialBackgroundColor)
    var backgroundColor: Kolor by backgroundColorProperty

    private fun randomByte() = randomIntInclusive(MAX_COLOR_VALUE)

    private fun randomKolor(): Kolor = Kolor(randomByte(), randomByte(), randomByte())

    override fun getFirstGeneration(): List<Kolor> = (1..generationSize).map { randomKolor() }

    override fun getCutPositions(): Pair<Int, Int> {
        val pos = randomIntExclusive(3)
        return Pair(pos, pos + 1)
    }

    override fun executeMutation(sequence: List<Int>): List<Int> = sequence.replace(randomIntExclusive(3), randomByte())

    override fun createNewChromosome(sequence: List<Int>): Kolor = Kolor(sequence[0], sequence[1], sequence[2])

    override fun calculateFitness(chromosome: Kolor): Double =
        1 / (1 + distanceCalulator.distance(chromosome, this.backgroundColor))

}
