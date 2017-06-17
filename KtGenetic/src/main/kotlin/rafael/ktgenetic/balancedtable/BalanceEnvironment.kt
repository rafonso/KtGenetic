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

    private val dimensions = BalanceDimensions(originalBoxes.size)

    private val originalBalance = Balance(originalBoxes.mapIndexed { index, weight -> Box(weight, index) }, dimensions)

    private var greatestMomentOfInertia: Double = 0.0

    override fun getFirstGeneration(): List<Balance> {
        val firstGeneration = mutableSetOf(originalBalance)

        while (firstGeneration.size < generationSize) {
            val temp = originalBalance.content.toMutableList()
            Collections.shuffle(temp, geneticRandom)
            firstGeneration.add(Balance(temp.toList(), dimensions))
        }

        return firstGeneration.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalBoxes.size)

    override fun executeMutation(sequence: Boxes): Boxes = sequence.randomSwap()

    override fun getNewGenotype(sequence: Boxes): Balance = Balance(sequence, dimensions)

    override fun calculateFitness(sequence: Boxes): Double {
        val bal = Balance(sequence, dimensions)
        val centerOfMassFitness = 1 - Math.abs(bal.centerOfMass - dimensions.center) / dimensions.center
        val momentOfInertiaFitness = 1 - bal.momentOfInertia / greatestMomentOfInertia

        return (centerOfMassFitness + momentOfInertiaFitness) / 2
    }

}
