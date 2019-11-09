package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.*
import kotlin.math.max

class ScreenEnvironment(
    private val originalBitmaps: Array<Array<Kolor>>,
    coverage: Double,
    override val maxGenerations: Int = Int.MAX_VALUE,
    override val generationSize: Int = 10,
    override var mutationFactor: Double = 0.01
) : Environment<Bitmap, Screen> {

    private val generator = PixelsGenerator(originalBitmaps.size, originalBitmaps[0].size, coverage)

    private val quantMutationPositions = max(1.0, 0.01 * generator.bitmapsSize).toInt()

    private fun randomByte() = randomIntInclusive(MAX_COLOR_VALUE)

    override fun getFirstGeneration(): List<Screen> =
        (0..generationSize).map { Screen(generator.createBitmaps()) }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(generator.bitmapsSize)

    override fun executeMutation(sequence: List<Bitmap>): List<Bitmap> {
        val seq = sequence.toMutableList()

        (1 until quantMutationPositions + 1)
            .map { geneticRandom.nextInt(sequence.size) }
            .map { seq[it] = sequence[it].copy(kolor = Kolor(randomByte(), randomByte(), randomByte())) }

        return seq
    }

    override fun createNewChromosome(sequence: List<Bitmap>): Screen = Screen(sequence)

    override fun calculateFitness(chromosome: Screen): Double {

        var distanceSum = 0.0

        fun calculateBitmapDistance(bitmap: Bitmap) {
            val pixelColor = originalBitmaps[bitmap.position.x][bitmap.position.y]

            bitmap.distance = pixelColor.distanceTo(bitmap.kolor)
            distanceSum += bitmap.distance
        }

        chromosome.content.forEach(::calculateBitmapDistance)

        return (MAX_COLOR_VALUE - (distanceSum / chromosome.content.size)) / MAX_COLOR_VALUE
    }

}