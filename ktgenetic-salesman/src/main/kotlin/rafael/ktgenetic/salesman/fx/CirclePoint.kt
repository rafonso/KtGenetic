package rafael.ktgenetic.salesman.fx

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

const val INITIAL_RADIO = 4.0

private val color = Color.BLACK

class CirclePoint(x: Double, y: Double) : Circle(x, y, INITIAL_RADIO, color) {
    init {
        onMouseEntered = EventHandler {
            println("Mouse Enter : ${it.x}, ${it.y}")
            scene.cursor = Cursor.OPEN_HAND
        }
        onMouseExited = EventHandler {
            println("Mouse Exited: ${it.x}, ${it.y}")
            scene.cursor = Cursor.DEFAULT
        }
        onMousePressed = EventHandler {
            println("Mouse Presse: ${it.x}, ${it.y}")
            scene.cursor = Cursor.CLOSED_HAND
        }
        onMouseReleased = EventHandler {
            println("Mouse Releas: ${it.x}, ${it.y}")
            scene.cursor = Cursor.OPEN_HAND
        }
        onMouseDragged = EventHandler { ev ->
            scene.cursor = Cursor.CROSSHAIR
            println("Mouse Draged: ${ev.x}, ${ev.y}")
            centerX = ev.x
            centerY = ev.y
        }
        onMouseDragReleased = EventHandler { ev ->
            println("Mouse Freed : ${ev.x}, ${ev.y}")
            scene.cursor = Cursor.DEFAULT
        }
        onMouseReleased = EventHandler {
            println("Mouse Releas: ${it.x}, ${it.y}")
            scene.cursor = Cursor.OPEN_HAND
        }
    }

    override fun toString(): String = "[centerX=%4.0f, centerY=%4.0f]".format(super.getCenterX(), super.getCenterY())

}