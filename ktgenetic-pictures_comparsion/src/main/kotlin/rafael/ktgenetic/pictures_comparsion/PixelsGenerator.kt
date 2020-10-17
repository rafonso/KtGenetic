package rafael.ktgenetic.pictures_comparsion

import rafael.ktgenetic.core.utils.randomIntExclusive
import rafael.ktgenetic.core.utils.randomIntInclusive
import kotlin.math.min
import kotlin.system.measureTimeMillis

private interface Generator {

    val bitmapsWidth: Int

    val bitmapsHeight: Int

    val bitmapsSize: Int

    fun createBitmaps(): List<Bitmap>

}

private class OnePixelGenerator(override val bitmapsWidth: Int, override val bitmapsHeight: Int) : Generator {

    override val bitmapsSize: Int = bitmapsWidth * bitmapsHeight

    override fun createBitmaps(): List<Bitmap> = (0 until bitmapsHeight).flatMap { y ->
        (0 until bitmapsWidth).map { x ->
            Bitmap(x, y, randomIntInclusive(255), randomIntInclusive(255), randomIntInclusive(255))
        }
    }

}

private class IntervalPixelGenerator(private val width: Int, private val height: Int, private val delta: Int) :
    Generator {

    override val bitmapsWidth: Int = width / delta

    override val bitmapsHeight: Int = height / delta

    override val bitmapsSize: Int = bitmapsHeight * bitmapsWidth

    override fun createBitmaps(): List<Bitmap> = (0 until height step delta).flatMap { y ->
        (0 until width step delta).map { x ->
            Bitmap(
                min(x + randomIntExclusive(delta), width - 1),
                min(y + randomIntExclusive(delta), height - 1),
                randomIntInclusive(255),
                randomIntInclusive(255),
                randomIntInclusive(255)
            )
        }
    }
}

class PixelsGenerator(width: Int, height: Int, coverage: Double) : Generator {

    private val generator: Generator =
        if (coverage <= 0.5) IntervalPixelGenerator(width, height, (1 / coverage).toInt())
        else OnePixelGenerator(width, height)

    override val bitmapsWidth: Int = generator.bitmapsWidth

    override val bitmapsHeight: Int = generator.bitmapsHeight

    override val bitmapsSize: Int = generator.bitmapsSize

    override fun createBitmaps(): List<Bitmap> = generator.createBitmaps()

}

fun main() {
    print("Width : ")
    val w = readLine()!!.toInt()
    print("Height: ")
    val h = readLine()!!.toInt()
    print("Cover : ")
    val c = readLine()!!.toDouble()

    val time = measureTimeMillis {
        val generator = PixelsGenerator(w, h, c)
        val pixels = generator.createBitmaps()

        println("bitmapsWidth : ${generator.bitmapsWidth} ")
        println("bitmapsHeight: ${generator.bitmapsHeight}")
        println("bitmapsSize  : ${generator.bitmapsSize}  ")
        println("pixels.size  : ${pixels.size}")
    }
    println(time)
}
