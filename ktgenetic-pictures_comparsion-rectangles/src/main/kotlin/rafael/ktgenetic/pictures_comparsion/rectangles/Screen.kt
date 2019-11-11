package rafael.ktgenetic.pictures_comparsion.rectangles

import rafael.ktgenetic.Chromosome

typealias Pixels = List<Rectangle>

data class Screen(override val content: Pixels) : Chromosome<Rectangle>()