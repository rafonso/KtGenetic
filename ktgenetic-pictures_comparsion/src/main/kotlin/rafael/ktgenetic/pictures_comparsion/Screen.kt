package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.Chromosome

typealias Pixels = List<Bitmap>

data class Screen(override val content: Pixels) : Chromosome<Bitmap>() {

    val distance: Double by lazy {
        content.map { it.distance }.average()
    }

}