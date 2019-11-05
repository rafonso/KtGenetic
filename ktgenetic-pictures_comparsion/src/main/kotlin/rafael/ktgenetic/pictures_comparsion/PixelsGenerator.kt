package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.randomIntExclusive
import rafael.ktgenetic.randomIntInclusive
import kotlin.system.measureTimeMillis

class PixelsGenerator(private val width: Int, private val height: Int, coverage: Double) {

    private val deltaCoverage = 1.0 - coverage

    private val deltaX = (1 / coverage).toInt()

    private val deltaY = (1 / coverage).toInt()

    fun createBitmaps(): List<Bitmap> {
//        println("deltaX=$deltaX")
//        println("deltaY=$deltaY")
        return (0 until height step deltaY).flatMap { y ->
            (0 until width step deltaX).map { x ->
//                println("\t($x, $y)")
                Bitmap(
                    x + randomIntExclusive(deltaX),
                    y + randomIntExclusive(deltaY),
                    randomIntInclusive(255),
                    randomIntInclusive(255),
                    randomIntInclusive(255)
                )
            }
        }
    }

}

/*
import rafael.ktgenetic.pictures_comparsion.*
kotlin.system.measureTimeMillis{
    println(PixelsGenerator(600, 400, 0.8).createBitmaps())
    println()
}
 */

fun main() {
    print("Width : ")
    val w = readLine()!!.toInt()
    print("Height: ")
    val h = readLine()!!.toInt()
    print("Cover : ")
    val c = readLine()!!.toDouble()

    val time = measureTimeMillis {
        val pixels = PixelsGenerator(w, h, c).createBitmaps()
        println(pixels)
        println(pixels.size)
    }
    println(time)
}