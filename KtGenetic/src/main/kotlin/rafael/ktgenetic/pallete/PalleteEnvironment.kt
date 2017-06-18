package rafael.ktgenetic.pallete

import rafael.ktgenetic.*
import java.util.*

class PalleteEnvironment(val originalBoxes: List<Int>,
                         val palleteDimension: PalleteDimensions,
                         override val maxGenerations: Int = Int.MAX_VALUE,
                         override val generationSize: Int = 10,
                         override var mutationFactor: Double = 0.02
) : Environment<Box, Pallete>, ProcessorListener {

    override fun onEvent(event: ProcessorEvent) {
        if (event.event == ProcessorEventEnum.ENDED_BY_INTERRUPTION ||
                event.event == ProcessorEventEnum.ENDED_BY_FITNESS ||
                event.event == ProcessorEventEnum.ENDED_BY_GENERATIONS
                ) {
            val chromosomes = event.value as List<Pallete>
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

    override fun getNewGenotype(sequence: Boxes): Pallete = Pallete(sequence, palleteDimension)

    override fun calculateFitness(sequence: Boxes): Double {
        val bal = Pallete(sequence, palleteDimension)
        val centerOfMassFitness = 1 - bal.centerOfMass.distance(palleteDimension.center) / palleteDimension.greatestDistanceFromCenter
        val momentOfInertiaFitness = 1 - bal.momentOfInertia / greatestMomentOfInertia

        return (centerOfMassFitness + momentOfInertiaFitness) / 2
    }

}