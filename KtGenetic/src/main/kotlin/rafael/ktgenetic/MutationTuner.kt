package rafael.ktgenetic

class MutationTuner<C: Chromosome<*>>(val environment: Environment<*, C>): ProcessorListener {

    val minimunVariation = 0.01

    val maximumVariation = 0.05

    private fun calculateVariationProportion(chromosomes: List<Chromosome<*>>): Double {
        val averageFitness = chromosomes.pMap { it.fitness }.sum() / chromosomes.size
        val averageFitnessDeviation = Math.sqrt(
                chromosomes.pMap { Math.pow(it.fitness - averageFitness, 2.0) }.sum() /
                        (chromosomes.size * (chromosomes.size - 1))
        )

        return averageFitnessDeviation / averageFitness
    }

    private fun adjustMutationFactor(proportion: Double) {
        if (proportion < minimunVariation && (environment.mutationFactor <= 0.99)) {
            environment.mutationFactor += 0.01
        } else if ((proportion > maximumVariation) && (environment.mutationFactor >= 0.01)) {
            environment.mutationFactor = Math.max(environment.mutationFactor - 0.01, 0.01)

        }
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.GENERATION_EVALUATED) {
            val proportion = calculateVariationProportion(event.population)
            adjustMutationFactor(proportion)
        }
    }

}