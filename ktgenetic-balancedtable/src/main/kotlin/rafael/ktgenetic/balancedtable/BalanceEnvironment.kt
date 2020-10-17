package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.createCutPositions
import rafael.ktgenetic.core.utils.geneticRandom
import rafael.ktgenetic.core.utils.randomSwap
import kotlin.math.abs

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
            temp.shuffle(geneticRandom)
            firstGeneration.add(Balance(temp.toList(), dimensions))
        }

        return firstGeneration.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(originalBoxes.size)

    override fun executeMutation(sequence: Boxes): Boxes = sequence.randomSwap()

    override fun createNewChromosome(sequence: Boxes): Balance = Balance(sequence, dimensions)

    override fun calculateFitness(chromosome: Balance): Double {
        val centerOfMassFitness = 1 - abs(chromosome.centerOfMass - dimensions.center) / dimensions.center
        val momentOfInertiaFitness = 1 - chromosome.momentOfInertia / greatestMomentOfInertia
        val balanceFitness = 1 - abs(chromosome.halfMasses.first - chromosome.halfMasses.second).toDouble() / chromosome.totalMass

        return (centerOfMassFitness + momentOfInertiaFitness + balanceFitness) / 3
    }

}
