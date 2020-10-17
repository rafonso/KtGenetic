package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.core.Chromosome

typealias Pixels = List<Bitmap>

data class Screen(override val content: Pixels) : Chromosome<Bitmap>()
