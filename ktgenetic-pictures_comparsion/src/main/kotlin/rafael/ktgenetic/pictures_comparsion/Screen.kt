package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.Chromosome

typealias Pixels = List<Bitmap>

data class Screen(override val content: Pixels) : Chromosome<Bitmap>() {

    fun distance(): Double = content.map { b -> b.distance }.average()

}