package rafael.ktgenetic.pallete

import rafael.ktgenetic.*
import java.util.*

class PalleteEnvironment(val originalBoxes: List<Int>,
                         val palleteDimension: PalleteDimensions,
                         override val maxGenerations: Int = Int.MAX_VALUE,
                         override val generationSize: Int = 10,
                         override var mutationFactor: Double = 0.02
) : Environment<Box, Pallete>, ProcessorListener {

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.ENDED_BY_INTERRUPTION ||
                event.eventType == TypeProcessorEvent.ENDED_BY_FITNESS ||
                event.eventType == TypeProcessorEvent.ENDED_BY_GENERATIONS
                ) {
            val chromosomes = event.population.filterIsInstance<Pallete>()
            mainLogger.info("Best pallete:\n${chromosomes[0].palleteToString}\n" +
                    "CM = ${chromosomes[0].centerOfMass}, " +
                    "MI = ${chromosomes[0].momentOfInertia}")
        }
    }

    private val originalBalance = Pallete(
            originalBoxes.mapIndexed { index, weight -> Box(index, weight) },
            palleteDimension
    )

    private val greatestMomentOfInertia: Double = originalBalance.totalMass *
            palleteDimension.greatestDistanceFromCenter * palleteDimension.greatestDistanceFromCenter

    override fun getFirstGeneration(): List<Pallete> {
        val firstGeneration = mutableSetOf(originalBalance)

        while (firstGeneration.size < generationSize) {
            val temp = originalBalance.content.toMutableList()
            Collections.shuffle(temp, geneticRandom)
            firstGeneration.add(Pallete(temp.toList(), palleteDimension))
        }

        return firstGeneration.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalBoxes.size)

    override fun executeMutation(sequence: Boxes): Boxes = sequence.randomSwap()

    override fun createNewChromosome(sequence: Boxes): Pallete = Pallete(sequence, palleteDimension)

    override fun calculateFitness(sequence: Boxes): Double {
        val pallete = Pallete(sequence, palleteDimension)
        val centerOfMassFitness = 1 - pallete.centerOfMass.distance(palleteDimension.center) / palleteDimension.greatestDistanceFromCenter
        val momentOfInertiaFitness = 1 - pallete.momentOfInertia / greatestMomentOfInertia
        val frontBackBalanceFitness = 1 - Math.abs(pallete.frontBackHalfMasses.first - pallete.frontBackHalfMasses.second).toDouble() / pallete.totalMass
        val rightLeftBalanceFitness = 1 - Math.abs(pallete.rightLeftHalfMasses.first - pallete.rightLeftHalfMasses.second).toDouble() / pallete.totalMass

        return (centerOfMassFitness + momentOfInertiaFitness + frontBackBalanceFitness + rightLeftBalanceFitness) / 4
    }

}

fun getPallete(weights: Collection<Int>, rows: Int, cols: Int): Pair<List<Int>, PalleteDimensions> {

    fun getPalleteDimensions(): Pair<Int, Int> {
        if (rows == 0 && cols == 0) {
            val sqrtSize = Math.sqrt(weights.size.toDouble()).toInt()
            val side: Int = sqrtSize + (if (weights.size == sqrtSize * sqrtSize) 0 else 1)
            return Pair(side, side)
        }
        if (rows == 0 && cols != 0) {
            return Pair((weights.size / cols) + (if (weights.size % cols == 0) 0 else 1), cols)
        }
        if (rows != 0 && cols == 0) {
            return Pair(rows, (weights.size / rows) + (if (weights.size % rows == 0) 0 else 1))
        }

        if ((rows * cols) < weights.size) {
            throw IllegalArgumentException("Number of Rows ($rows) times the number of columns ($cols) " +
                    "lesser than the size of weights (${weights.size})")
        }

        return Pair(rows, cols)
    }

    fun getNormalizedPallete(r: Int, c: Int): List<Int> {
        if (weights.size == r * c) {
            return weights.toList()
        }

        val result = IntArray(c * r)
        weights.forEachIndexed { index, w -> result[index] = w }
        return result.toList()
    }

    val (r, c) = getPalleteDimensions()
    val normalizedPallet = getNormalizedPallete(r, c)

    return Pair(normalizedPallet, PalleteDimensions(r, c))
}