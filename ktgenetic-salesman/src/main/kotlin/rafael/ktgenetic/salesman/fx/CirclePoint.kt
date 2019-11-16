package rafael.ktgenetic.salesman.fx

import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

const val INITIAL_RADIO = 4.0

private val color = Color.BLACK

class CirclePoint(x: Double, y: Double) : Circle(x, y, INITIAL_RADIO, color) {

    init {
        addEventHandler(MouseEvent.ANY, CirclePointMouseEventHandler)
    }

    override fun toString(): String = "[centerX=%4.0f, centerY=%4.0f]".format(super.getCenterX(), super.getCenterY())

}

object CirclePointMouseEventHandler : EventHandler<MouseEvent> {

    private val eventTypeToCursor: Map<EventType<MouseEvent>, Cursor> = mapOf(
        MouseEvent.MOUSE_DRAGGED to Cursor.CROSSHAIR,
        MouseEvent.MOUSE_ENTERED to Cursor.OPEN_HAND,
        MouseEvent.MOUSE_EXITED to Cursor.DEFAULT,
        MouseEvent.MOUSE_PRESSED to Cursor.CLOSED_HAND,
        MouseEvent.MOUSE_RELEASED to Cursor.OPEN_HAND
    )


    override fun handle(event: MouseEvent?) {
        val source = event?.source as CirclePoint
        source.scene.cursor = eventTypeToCursor.getOrDefault(event.eventType, source.scene.cursor)
        when (event.eventType) {
            MouseEvent.MOUSE_DRAGGED -> {
                source.centerX = event.x
                source.centerY = event.y
            }
        }
    }

}