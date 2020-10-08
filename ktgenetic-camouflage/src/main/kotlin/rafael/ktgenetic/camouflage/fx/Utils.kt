package rafael.ktgenetic.camouflage.fx

import javafx.scene.paint.Color
import rafael.ktgenetic.camouflage.MAX_COLOR_VALUE

fun formatRgbColor(c: Color) = "RGB(%03d, %03d, %03d)".format(
    (c.red * MAX_COLOR_VALUE).toInt(),
    (c.green * MAX_COLOR_VALUE).toInt(),
    (c.blue * MAX_COLOR_VALUE).toInt(),
)

fun formatHsbColor(c: Color) = "HSB(%03.0f°, %.2f, %.2f)".format(
    c.hue,
    c.saturation,
    c.brightness,
)

fun formatHexColor(c: Color) = "#%02x%02x%02x".format(
    (c.red * MAX_COLOR_VALUE).toInt(),
    (c.green * MAX_COLOR_VALUE).toInt(),
    (c.blue * MAX_COLOR_VALUE).toInt(),
)
