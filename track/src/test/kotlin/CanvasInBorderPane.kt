import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

class CanvasInBorderPane : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val borderPane = BorderPane()

        // Put menu bar on the top of the window
        val menuBar = MenuBar(Menu("File"), Menu("Edit"),
                Menu("Help"))
        //        borderPane.setTop(menuBar);
        val label = Label("teste")

        // Create a wrapper Pane first
        val wrapperPane = Pane()
        borderPane.center = wrapperPane
        borderPane.bottom = label
        // Put canvas in the center of the window (*)
        val canvas = Canvas()
        wrapperPane.children.add(canvas)
        // Bind the width/height property so that the size of the Canvas will be
        // resized as the window is resized
        canvas.widthProperty().bind(wrapperPane.widthProperty())
        canvas.heightProperty().bind(wrapperPane.heightProperty())
        // redraw when resized
        canvas.widthProperty().addListener { event -> draw(canvas) }
        canvas.heightProperty().addListener { event -> draw(canvas) }
        canvas.setOnMouseMoved { event ->
            val y = canvas.height - event.y
            label.text = event.x.toString() + " x " + y
        }
        canvas.setOnMouseExited { event -> label.text = "" }
        draw(canvas)

        val scene = Scene(borderPane)
        primaryStage.scene = scene
        primaryStage.width = 400.0
        primaryStage.height = 300.0
        primaryStage.show()
    }

    /**
     * https://stackoverflow.com/questions/37678704/how-to-embed-javafx-canvas-into-borderpane

     * Draw crossed red lines which each each end is at the corner of window,
     * and 4 blue circles whose each center is at the corner of the window,
     * so that make it possible to know where is the extent the Canvas draws
     */
    private fun draw(canvas: Canvas) {
        val width = canvas.width.toInt()
        val height = canvas.height.toInt()
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, width.toDouble(), height.toDouble())
        gc.stroke = Color.RED
        gc.strokeLine(0.0, 0.0, width.toDouble(), height.toDouble())
        gc.strokeLine(0.0, height.toDouble(), width.toDouble(), 0.0)
        gc.fill = Color.BLUE
        gc.fillOval(-30.0, -30.0, 60.0, 60.0)
        gc.fillOval((-30 + width).toDouble(), -30.0, 60.0, 60.0)
        gc.fillOval(-30.0, (-30 + height).toDouble(), 60.0, 60.0)
        gc.fillOval((-30 + width).toDouble(), (-30 + height).toDouble(), 60.0, 60.0)
    }

}
