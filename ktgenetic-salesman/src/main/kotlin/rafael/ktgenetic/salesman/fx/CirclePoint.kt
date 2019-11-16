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
            scene.cursor = Cursor.OPEN_HAND
        }
        onMouseExited = EventHandler {
            scene.cursor = Cursor.DEFAULT
        }
        onMousePressed = EventHandler {
            scene.cursor = Cursor.CLOSED_HAND
        }
        onMouseReleased = EventHandler {
            scene.cursor = Cursor.OPEN_HAND
        }
        onMouseDragged = EventHandler { ev ->
            scene.cursor = Cursor.CROSSHAIR
            centerX = ev.x
            centerY = ev.y
        }
        onMouseDragReleased = EventHandler {
            scene.cursor = Cursor.DEFAULT
        }
        onMouseReleased = EventHandler {
            scene.cursor = Cursor.OPEN_HAND
        }
    }

    override fun toString(): String = "[centerX=%4.0f, centerY=%4.0f]".format(super.getCenterX(), super.getCenterY())

}