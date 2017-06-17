package rafael.ktgenetic.pallete

import rafael.ktgenetic.*
import java.util.*

class PalleteEnvironment(val originalBoxes: List<Int>,
                         val palleteDimension: PalleteDimensions,
                         override val mutationFactor: Double = 0.01,
                         override val maxGenerations: Int = Int.MAX_VALUE,
                         override val generationSize: Int = 10
) : Environment<Box, Pallete>, ProcessorListener {

    override fun onEvent(event: ProcessorEvent) {
        if (event.event == ProcessorEventEnum.FITNESS_CALCULATING) {
            val chromosomes = event.value as List<Pallete>
            greatestMomentOfInertia = chromosomes.map { it.momentOfInertia }.max() as Double
        }
    }

    private val originalBalance = Pallete(
            originalBoxes.mapIndexed { index, weight -> Box(index, weight) },
            palleteDimension
    )

    private var greatestMomentOfInertia: Double = 0.0

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

    override fun getNewGenotype(sequence: Boxes): Pallete = Pallete(sequence, palleteDimension)

    override fun calculateFitness(sequence: Boxes): Double {
        val bal = Pallete(sequence, palleteDimension)
        val centerOfMassFitness = 1 - bal.centerOfMass.distance(palleteDimension.center) / palleteDimension.greatestDistanceFromCenter
        val momentOfInertiaFitness = 1 - bal.momentOfInertia / greatestMomentOfInertia

        return (centerOfMassFitness + momentOfInertiaFitness) / 2
    }

}