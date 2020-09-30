package rafael.ktgenetic.camouflage

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

enum class KolorDistance(description: String) {

    RGB("RGB") {
        override fun distance(k1: Kolor, k2: Kolor): Double {
            val deltaR = (k1.color.red - k2.color.red)
            val deltaG = (k1.color.green - k2.color.green)
            val deltaB = (k1.color.blue - k2.color.blue)

            return sqrt((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB))
        }
    },
    HSL("HSL") {
        override fun distance(k1: Kolor, k2: Kolor): Double {
            val deltaH = min(abs(k1.color.hue - k2.color.hue), 360 - abs(k1.color.hue - k2.color.hue)) / 180
            val deltaS = (k1.color.saturation - k2.color.saturation)
            val deltaL = (k1.color.brightness - k2.color.brightness)

            return sqrt((deltaH * deltaH) + (deltaS * deltaS) + (deltaL * deltaL))
        }
    };

    abstract fun distance(k1: Kolor, k2: Kolor): Double

}
