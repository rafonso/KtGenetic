package rafael.ktgenetic.pictures_comparsion.rectangles

import rafael.ktgenetic.core.Chromosome

/**
 * Represents a list of rectangles.
 */
typealias Pixels = List<Rectangle>

/**
 * Represents a screen as a chromosome of rectangles.
 *
 * @property content The list of rectangles that make up the screen.
 */
data class Screen(override val content: Pixels) : Chromosome<Rectangle>()
