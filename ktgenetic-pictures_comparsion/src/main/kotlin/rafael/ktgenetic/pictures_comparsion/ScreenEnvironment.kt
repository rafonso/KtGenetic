package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.Environment
import rafael.ktgenetic.*

class ScreenEnvironment(private val originalPicture: Pixels,
                        private val width: Int, private val height: Int,
                        override val maxGenerations: Int = Int.MAX_VALUE,
                        override val generationSize: Int = 10,
                        override var mutationFactor: Double = 0.01) : Environment<Bitmap, Screen> {

    override fun getFirstGeneration(): List<Screen> =
            (0..generationSize).map {
                Screen(
                        (0..width).flatMap { x ->
                            (0..height).map { y ->
                                Bitmap(x, y, geneticRandom.nextInt(256), geneticRandom.nextInt(256), geneticRandom.nextInt(256))
                            }
                        })
            }


    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalPicture.size)

    override fun executeMutation(sequence: List<Bitmap>): List<Bitmap> {
        val mutationPos = geneticRandom.nextInt(sequence.size)
        val mutationPixel = sequence[mutationPos].copy(r = geneticRandom.nextInt(256), g = geneticRandom.nextInt(256), b = geneticRandom.nextInt(256))

        return sequence.replace(mutationPos, mutationPixel)
    }

    override fun createNewChromosome(sequence: List<Bitmap>): Screen = Screen(sequence)

    override fun calculateFitness(chromosome: Screen): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resultFound(genotypes: List<Screen>): Boolean = (genotypes[0].distance() == 0.0)

}