package rafael.ktgenetic.pictures_comparsion.rectangles

import rafael.ktgenetic.*

class ScreenEnvironment(
    originalBitmaps: Array<Array<Kolor>>,
    rows: Int, cols: Int,
    override val maxGenerations: Int = Int.MAX_VALUE,
    override val generationSize: Int = 10,
    override var mutationFactor: Double = 0.01
) : Environment<Rectangle, Screen> {

    private fun randomByte() = randomIntInclusive(MAX_COLOR_VALUE)

    private fun randomKolor(): Kolor = Kolor(randomByte(), randomByte(), randomByte())

    private val originalRectangles: Pixels = generateOriginalRectangles(
        originalBitmaps,
        rows, cols
    )

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


    override fun getFirstGeneration(): List<Screen> = (0..generationSize).map {
        originalRectangles.map {
            it.copy(
                kolor = randomKolor()
            )
        }
    }.map { Screen(it) }


    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalRectangles.size)

    override fun executeMutation(sequence: List<Rectangle>): List<Rectangle> {
        val pos = randomIntExclusive(sequence.size)
        val kolor = randomKolor()
        val rect = sequence[pos].copy(kolor = kolor)

        return sequence.replace(pos, rect)
    }

    override fun createNewChromosome(sequence: List<Rectangle>): Screen = Screen(sequence)

    override fun calculateFitness(chromosome: Screen): Double {

        val averageDistance = chromosome.content.mapIndexed { index, rect ->
            rect.kolor.distanceTo(originalRectangles[index].kolor)
        }.average()

        return (MAX_COLOR_VALUE_D - averageDistance) / MAX_COLOR_VALUE_D
    }
}