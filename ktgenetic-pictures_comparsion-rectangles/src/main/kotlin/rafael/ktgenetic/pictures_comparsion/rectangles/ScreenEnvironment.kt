package rafael.ktgenetic.pictures_comparsion.rectangles

import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.createCutPositions
import rafael.ktgenetic.core.utils.randomIntExclusive
import rafael.ktgenetic.core.utils.randomIntInclusive
import rafael.ktgenetic.core.utils.replace

/**
 * Represents the environment for a screen of rectangles.
 *
 * @property maxGenerations The maximum number of generations.
 * @property generationSize The size of each generation.
 * @property mutationFactor The mutation factor.
 */
class ScreenEnvironment(
    originalBitmaps: Array<Array<Kolor>>,
    rows: Int, cols: Int,
    override val maxGenerations: Int = Int.MAX_VALUE,
    override val generationSize: Int = 10,
    override var mutationFactor: Double = 0.01
) : Environment<Rectangle, Screen> {


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
     * The original rectangles of the screen.
     */
    private val originalRectangles: Pixels = generateOriginalRectangles(
        originalBitmaps,
        rows, cols
    )

    /**
     * Generates the original rectangles of the screen.
     *
     * @param originalBitmaps The original bitmaps.
     * @param rows The number of rows.
     * @param cols The number of columns.
     * @return The original rectangles of the screen.
     */
    private fun generateOriginalRectangles(
        originalBitmaps: Array<Array<Kolor>>,
        rows: Int,
        cols: Int
    ): List<Rectangle> {

        fun averageColor(h0: Int, h1: Int, w0: Int, w1: Int, extractor: (Kolor) -> Int) =
            (h0 until h1).flatMap { h ->
                (w0 until w1).map { w ->
                    originalBitmaps[h][w]
                }.map(extractor)
            }.average().toInt()


        val deltaW = originalBitmaps[0].size / cols
        val deltaH = originalBitmaps.size / rows

        return (0 until cols).flatMap { cl ->
            val w0 = cl * deltaW
            val w1 = w0 + deltaW
            (0 until rows).map { rw ->
                val h0 = rw * deltaH
                val h1 = h0 + deltaH

                val r = averageColor(h0, h1, w0, w1, Kolor::r)
                val g = averageColor(h0, h1, w0, w1, Kolor::g)
                val b = averageColor(h0, h1, w0, w1, Kolor::b)

                Rectangle(Position(w0, h0), Position(w1, h1), Kolor(r, g, b))
            }
        }
    }


    /**
     * Generates the first generation of screens.
     *
     * @return The first generation of screens.
     */
    override fun getFirstGeneration(): List<Screen> = List(generationSize) {
        Screen(originalRectangles.map { it.copy(kolor = randomKolor()) })
    }

    /**
     * Generates the cut positions for the genetic algorithm.
     *
     * @return The cut positions for the genetic algorithm.
     */
    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalRectangles.size)

    /**
     * Executes a mutation on a sequence of rectangles.
     *
     * @param sequence The sequence of rectangles.
     * @return The mutated sequence of rectangles.
     */
    override fun executeMutation(sequence: List<Rectangle>): List<Rectangle> {
        val pos = randomIntExclusive(sequence.size)
        val kolor = randomKolor()
        val rect = sequence[pos].copy(kolor = kolor)

        return sequence.replace(pos, rect)
    }

    /**
     * Creates a new chromosome from a sequence of rectangles.
     *
     * @param sequence The sequence of rectangles.
     * @return The new chromosome.
     */
    override fun createNewChromosome(sequence: List<Rectangle>): Screen = Screen(sequence)

    /**
     * Calculates the fitness of a chromosome.
     *
     * @param chromosome The chromosome.
     * @return The fitness of the chromosome.
     */
    override fun calculateFitness(chromosome: Screen): Double {
        var average = 0.0
        val size = chromosome.content.size
        chromosome.content.forEachIndexed { index, rect ->
            average += rect.kolor.distanceTo(originalRectangles[index].kolor).toDouble() / size
        }

        return (MAX_COLOR_VALUE_D - average) / MAX_COLOR_VALUE_D
    }
}
