package rafael.ktgenetic.core.processor

import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.TemplateChromosome

class GeneticCrosserTestEnvironmentStub : Environment<Char, TemplateChromosome> {

    override var mutationFactor: Double
        get() = throw NotImplementedError("Not Used")
        set(@Suppress("UNUSED_PARAMETER") value) {
            throw NotImplementedError("Not Used")
        }

    override val maxGenerations: Int
        get() = throw NotImplementedError("Not Used")

    override val generationSize: Int
        get() = throw NotImplementedError("Not Used")

    override fun getFirstGeneration(): List<TemplateChromosome> {
        throw NotImplementedError("Not Used")
    }

    override fun getCutPositions(): Pair<Int, Int> {
        throw NotImplementedError("Not Used")
    }

    override fun executeMutation(sequence: List<Char>): List<Char> {
        throw NotImplementedError("Not Used")
    }

    override fun createNewChromosome(sequence: List<Char>): TemplateChromosome = TemplateChromosome(sequence, 0.0)

    override fun calculateFitness(chromosome: TemplateChromosome): Double = chromosome.fitness

}
