package rafael.ktgenetic.salesman.fx

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

const val INITIAL_RADIO = 4.0

private val color = Color.BLACK

enum class CirclePointType {
    COMMON, START, END
}

class CirclePoint(
    x: Double,
    y: Double,
    val typeProperty: ObjectProperty<CirclePointType> = SimpleObjectProperty(CirclePointType.COMMON)
) : Circle(x, y, INITIAL_RADIO, color) {

    init {
        addEventHandler(MouseEvent.ANY, CirclePointMouseEventHandler)
        typeProperty.addListener { _, _, newType: CirclePointType ->
            fill =
                    when (newType) {
                        CirclePointType.COMMON -> Color.BLACK
                        CirclePointType.START  -> Color.BLUE
                        CirclePointType.END    -> Color.RED
                    }
        }
    }

    override fun toString(): String =
            "[centerX=%4.0f, centerY=%4.0f, %3s]".format(super.getCenterX(), super.getCenterY(), typeProperty.value)

}

object CirclePointMouseEventHandler : EventHandler<MouseEvent> {

    private val eventTypeToCursor: Map<EventType<MouseEvent>, Pair<Cursor, Double>> = mapOf(
        MouseEvent.MOUSE_DRAGGED to (Cursor.CROSSHAIR to (INITIAL_RADIO + 1)),
        MouseEvent.MOUSE_ENTERED to (Cursor.OPEN_HAND to (INITIAL_RADIO + 1)),
        MouseEvent.MOUSE_EXITED to (Cursor.DEFAULT to (INITIAL_RADIO)),
        MouseEvent.MOUSE_PRESSED to (Cursor.CLOSED_HAND to (INITIAL_RADIO + 1)),
        MouseEvent.MOUSE_RELEASED to (Cursor.OPEN_HAND to (INITIAL_RADIO + 1))
    )


    override fun handle(event: MouseEvent?) {
        val source = event?.source as CirclePoint
        eventTypeToCursor.getOrDefault(event.eventType, (source.scene.cursor to source.radius)).also {
            source.scene.cursor = it.first
            source.radius = it.second
        }
        when (event.eventType) {
            MouseEvent.MOUSE_DRAGGED -> {
                source.centerX = event.x
                source.centerY = event.y
            }
        }
    }

}