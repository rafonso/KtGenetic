package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.Environment
import rafael.ktgenetic.*
import kotlin.math.sqrt

class ScreenEnvironment(
    private val originalBitmaps: Array<Array<Triple<Int, Int, Int>>>,
    coverage: Double,
    override val maxGenerations: Int = Int.MAX_VALUE,
    override val generationSize: Int = 10,
    override var mutationFactor: Double = 0.01
) : Environment<Bitmap, Screen> {

    private val generator = PixelsGenerator(originalBitmaps.size, originalBitmaps[0].size, coverage)

    private fun randomByte() = randomIntInclusive(255)

    override fun getFirstGeneration(): List<Screen> =
        (0..generationSize).map { Screen(generator.createBitmaps()) }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(generator.bitmapsSize)

    override fun executeMutation(sequence: List<Bitmap>): List<Bitmap> {
        val mutationPos = geneticRandom.nextInt(sequence.size)
        val mutationPixel = sequence[mutationPos].copy(r = randomByte(), g = randomByte(), b = randomByte())

        return sequence.replace(mutationPos, mutationPixel)
    }

    override fun createNewChromosome(sequence: List<Bitmap>): Screen = Screen(sequence)

    override fun calculateFitness(chromosome: Screen): Double {

        fun calculateBitmapDistance(bitmap: Bitmap) {
            val pixel = originalBitmaps[bitmap.x][bitmap.y]

            val deltaR = bitmap.r - pixel.first
            val deltaG = bitmap.g - pixel.second
            val deltaB = bitmap.b - pixel.third

            val distanceSquare = (deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB)
            bitmap.distance = sqrt(distanceSquare.toDouble())
        }

        chromosome.content.forEach(::calculateBitmapDistance)

        return (255.0 - chromosome.distance) / 255
    }

}