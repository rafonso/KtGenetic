package rafael.ktgenetic.camouflage

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Enum class representing different methods of calculating color distance.
 *
 * @property description A brief description of the color distance method.
 */
enum class KolorDistance(val description: String) {

    /**
     * RGB method of calculating color distance.
     *
     * The RGB color model is an additive color model in which the red, green, and blue primary colors of light are
     * added together in various ways to reproduce a broad array of colors.
     */
    RGB("RGB") {
        /**
         * Calculates the distance between two colors in the RGB color space.
         * This "distance" is calculated such as tha distance of 2 points.
         *
         * @param k1 The first color.
         * @param k2 The second color.
         * @return The distance between the two colors.
         */
        override fun distance(k1: Kolor, k2: Kolor): Double {
            val deltaR = (k1.color.red - k2.color.red)
            val deltaG = (k1.color.green - k2.color.green)
            val deltaB = (k1.color.blue - k2.color.blue)

            return sqrt((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB))
        }
    },

    /**
     * HSL method of calculating color distance.
     * This "distance" is calculated such as tha distance of 2 points.
     *
     * The HSL color model is a color model that uses hue, saturation, and lightness.
     */
    HSL("HSL") {
        /**
         * Calculates the distance between two colors in the HSL color space.
         *
         * @param k1 The first color.
         * @param k2 The second color.
         * @return The distance between the two colors.
         */
        override fun distance(k1: Kolor, k2: Kolor): Double {
            val deltaH = min(abs(k1.color.hue - k2.color.hue), 360 - abs(k1.color.hue - k2.color.hue)) / 180
            val deltaS = (k1.color.saturation - k2.color.saturation)
            val deltaL = (k1.color.brightness - k2.color.brightness)

            return sqrt((deltaH * deltaH) + (deltaS * deltaS) + (deltaL * deltaL))
        }
    };

    /**
     * Calculate the distance between two colors.
     *
     * @param k1 The first color.
     * @param k2 The second color.
     * @return The distance between the two colors.
     */
    abstract fun distance(k1: Kolor, k2: Kolor): Double

}