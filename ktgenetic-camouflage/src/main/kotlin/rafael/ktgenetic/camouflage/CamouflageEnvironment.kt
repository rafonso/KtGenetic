package rafael.ktgenetic.camouflage

import javafx.beans.property.SimpleObjectProperty
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.randomIntExclusive
import rafael.ktgenetic.core.utils.randomIntInclusive
import rafael.ktgenetic.core.utils.replace
import tornadofx.getValue
import tornadofx.setValue

/**
 * Represents the environment for the camouflage genetic algorithm.
 *
 * @param initialBackgroundColor The initial background color.
 * @property distanceCalculator The method used to calculate color distance.
 * @property maxGenerations The maximum number of generations.
 * @property generationSize The size of each generation.
 * @property mutationFactor The mutation factor.
 */
class CamouflageEnvironment(
    initialBackgroundColor: Kolor,
    private val distanceCalculator: KolorDistance,
    override val maxGenerations: Int,
    override val generationSize: Int,
    override var mutationFactor: Double = 0.01
) : Environment<Int, Kolor> {

    // The background color property.
    private val backgroundColorProperty = SimpleObjectProperty(initialBackgroundColor)
    // The background color.
    var backgroundColor: Kolor by backgroundColorProperty

    /**
     * Generates a random byte.
     *
     * @return A random byte.
     */
    private fun randomByte() = randomIntInclusive(MAX_COLOR_VALUE)

    /**
     * Generates a random color.
     *
     * @return A random color.
     */
    private fun randomKolor(): Kolor = Kolor(randomByte(), randomByte(), randomByte())

    /**
     * Generates the first generation of colors.
     *
     * @return The first generation of colors.
     */
    override fun getFirstGeneration(): List<Kolor> = (1..generationSize).map { randomKolor() }

    /**
     * Gets the cut positions for the genetic algorithm.
     *
     * @return The cut positions.
     */
    override fun getCutPositions(): Pair<Int, Int> {
        val pos = randomIntExclusive(3)
        return Pair(pos, pos + 1)
    }

    /**
     * Executes a mutation on a sequence of integers.
     *
     * @param sequence The sequence to mutate.
     * @return The mutated sequence.
     */
    override fun executeMutation(sequence: List<Int>): List<Int> = sequence.replace(randomIntExclusive(3), randomByte())

    /**
     * Creates a new color from a sequence of integers.
     *
     * @param sequence The sequence of integers.
     * @return The new color.
     */
    override fun createNewChromosome(sequence: List<Int>): Kolor = Kolor(sequence[0], sequence[1], sequence[2])

    /**
     * Calculates the fitness of a color.
     *
     * @param chromosome The color to calculate the fitness of.
     * @return The fitness of the color.
     */
    override fun calculateFitness(chromosome: Kolor): Double =
        1 / (1 + distanceCalculator.distance(chromosome, this.backgroundColor))

}