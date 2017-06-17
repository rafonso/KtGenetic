package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.*
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

    private val originalBalance = Balance(originalBoxes.mapIndexed { index, weight -> Box(weight, index) })

    private var greatestMomentOfInertia: Double = 0.0

    override fun getFirstGeneration(): List<Balance> {
        val firstGeneration = mutableSetOf(originalBalance)

        while (firstGeneration.size < generationSize) {
            val temp = originalBalance.content.toMutableList()
            Collections.shuffle(temp, geneticRandom)
            firstGeneration.add(Balance(temp.toList()))
        }

        return firstGeneration.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalBoxes.size)

    override fun executeMutation(sequence: Boxes): Boxes = sequence.randomSwap()

    override fun getNewGenotype(sequence: Boxes): Balance = Balance(sequence)

    override fun calculateFitness(sequence: Boxes): Double {
        val bal = Balance(sequence)
        val centerOfMassFitness = 1 - 2 / sequence.size * Math.abs(bal.centerOfMass - sequence.size.toDouble() / 2)
        val momentOfInertiaFitness = 1 - bal.momentOfInertia / greatestMomentOfInertia

        return (centerOfMassFitness + momentOfInertiaFitness) / 2
    }

}
