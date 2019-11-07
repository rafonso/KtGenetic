package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.*

class ScreenEnvironment(
    private val originalBitmaps: Array<Array<Kolor>>,
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
        val mutationPixel = sequence[mutationPos].copy(kolor = Kolor(randomByte(), randomByte(), randomByte()))

        return sequence.replace(mutationPos, mutationPixel)
    }

    override fun createNewChromosome(sequence: List<Bitmap>): Screen = Screen(sequence)

    override fun calculateFitness(chromosome: Screen): Double {

        fun calculateBitmapDistance(bitmap: Bitmap) {
            val pixelColor = originalBitmaps[bitmap.position.x][bitmap.position.y]

            bitmap.distance = pixelColor.distanceTo(bitmap.kolor)
        }

        chromosome.content.forEach(::calculateBitmapDistance)

        return (255.0 - chromosome.distance) / 255
    }

}