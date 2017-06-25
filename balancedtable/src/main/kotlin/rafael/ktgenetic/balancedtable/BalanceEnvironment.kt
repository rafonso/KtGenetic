package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.geneticRandom
import rafael.ktgenetic.randomSwap
import java.util.*

class BalanceEnvironment(val originalBoxes: List<Int>,
                         override val maxGenerations: Int = Int.MAX_VALUE,
                         override val generationSize: Int = 10,
                         override var mutationFactor: Double = 0.01
) : Environment<Box, Balance> {
    private val dimensions = BalanceDimensions(originalBoxes.size)

    private val originalBalance = Balance(originalBoxes.mapIndexed { index, weight -> Box(index, weight) }, dimensions)

    private val greatestMomentOfInertia: Double = originalBalance.totalMass * (dimensions.center * dimensions.center)

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

    override fun createNewChromosome(sequence: Boxes): Balance = Balance(sequence, dimensions)

    override fun calculateFitness(sequence: Boxes): Double {
        val bal = Balance(sequence, dimensions)
        val centerOfMassFitness = 1 - Math.abs(bal.centerOfMass - dimensions.center) / dimensions.center
        val momentOfInertiaFitness = 1 - bal.momentOfInertia / greatestMomentOfInertia
        val balanceFitness = 1 - Math.abs(bal.halfMasses.first - bal.halfMasses.second).toDouble() / bal.totalMass

        return (centerOfMassFitness + momentOfInertiaFitness + balanceFitness) / 3
    }

}
