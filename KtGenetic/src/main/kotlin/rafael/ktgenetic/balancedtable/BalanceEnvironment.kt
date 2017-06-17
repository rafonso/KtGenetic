package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Environment
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.ProcessorEventEnum
import rafael.ktgenetic.ProcessorListener
import java.lang.Math.abs
import java.util.*

class BalanceEnvironment(val originalBoxes: List<Int>,
                         override val mutationFactor: Double = 0.01,
                         override val maxGenerations: Int = Int.MAX_VALUE,
                         override val generationSize: Int = 10
) : Environment<Box, Balance>, ProcessorListener {

    override fun onEvent(event: ProcessorEvent) {
        if (event.event == ProcessorEventEnum.FITNESS_CALCULATING) {
            val chromosomes = event.value as List<Balance>
            greatestMomentOfInertia = chromosomes.map { it.momentOfInertia }.max() as Double
        }
    }

    private val random = Random()

    private val originalBalance = Balance(originalBoxes.mapIndexed { index, weight -> Box(weight, index) })

    private var greatestMomentOfInertia: Double = 0.0

    private fun scramble(originalSegment: Boxes): Boxes {
        when (originalSegment.size) {
            1 -> return originalSegment
            2 -> return listOf(originalSegment[1], originalSegment[0])
            else -> {
                val pos1 = random.nextInt(originalSegment.size - 2)
                val pos2 = if (pos1 == (originalSegment.size - 2)) (originalSegment.size - 1)
                else (pos1 + 1 + random.nextInt(originalSegment.size - 1 - pos1))

                val copy = originalSegment.toMutableList()
                val temp = copy[pos1]
                copy[pos1] = copy[pos2]
                copy[pos2] = temp

                return copy.toList()
            }
        }
    }

    override fun getFirstGeneration(): List<Balance> {
        val firstGeneration = mutableSetOf(originalBalance)

        while (firstGeneration.size < generationSize) {
            val temp = originalBalance.content.toMutableList()
            Collections.shuffle(temp, random)
            firstGeneration.add(Balance(temp.toList()))
        }

        return firstGeneration.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> {
        val pos1 = 1 + random.nextInt(originalBoxes.size - 2)
        val pos2 = 1 + random.nextInt(originalBoxes.size - 1 - pos1) + pos1

        return Pair(pos1, pos2)
    }

    override fun cutIntoPieces(sequence: Boxes, cutPositions: Pair<Int, Int>): Triple<Boxes, Boxes, Boxes> =
            Triple(
                    sequence.subList(0, cutPositions.first),
                    sequence.subList(cutPositions.first, cutPositions.second),
                    sequence.subList(cutPositions.second, sequence.size)
            )

    override fun executeMutation(sequence: Boxes): Boxes = scramble(sequence)

    override fun getNewGenotype(sequence: Boxes): Balance = Balance(sequence)

    override fun calculateFitness(sequence: Boxes): Double {
        val bal = Balance(sequence)
        val centerOfMassFitness = 1 - 2 / sequence.size * abs(bal.centerOfMass - sequence.size.toDouble() / 2)
        val momentOfInertiaFitness = 1 - bal.momentOfInertia / greatestMomentOfInertia
        return (centerOfMassFitness + momentOfInertiaFitness) / 2
    }

}
