package rafael.ktgenetic.camouflage.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import rafael.ktgenetic.Environment
import rafael.ktgenetic.camouflage.Kolor
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class CamouflageApp : App(CamouflageView::class)

class CamouflageView : GeneticView<Int, Kolor>("Camouflage", GeneticProcessorChoice.SIMPLE) {

    // INPUT COMPONENTS

    private val backgroundColorPicker = colorpicker(Color.WHITE, ColorPickerMode.Button).also { colorPicker ->
        colorPicker.addEventFilter(KeyEvent.KEY_PRESSED) { event ->
            if (event.code == KeyCode.ESCAPE) {
                // Avoids call resetComponents()
                event.consume()
            }
        }
    }

    private val chkNonStop = checkbox("Run non Stop")

    // OUTPUT COMPONENTS

    private val pnlEnvironment = flowpane {
        padding = insets(5)
        vgap = 5.0
        hgap = 5.0
    }

    private val cmbCircleRadius = combobox<Int> {
        items = observableListOf(10, 20, 30, 40, 50)
        value = 10
    }

    // OTHER COMPONENTS

    private val circlesProperty = SimpleObjectProperty<List<Circle>>(listOf<Circle>())
    private var circles by circlesProperty


    init {
        backgroundColorPicker.onAction = EventHandler { changeBackground() }

        addComponent("Background Color", backgroundColorPicker)
        addComponent(chkNonStop)

        cmbCircleRadius.onAction = EventHandler { event ->
            val newRadius = cmbCircleRadius.value.toDouble()
            this.circles.forEach { it.radius = newRadius }
            reloadBackgound()
        }
        addComponent("Circles Radi", cmbCircleRadius)


        circlesProperty.onChange { reloadBackgound() }

        val scrollPane = scrollpane(true, true) {
            content = pnlEnvironment
        }
        root.center = scrollPane

        changeBackground()
    }

    private fun reloadBackgound() {
        pnlEnvironment.clear()
        this.circles.forEach { pnlEnvironment.add(it) }
    }

    private fun changeBackground() {
        pnlEnvironment.background =
            Background(BackgroundFill(backgroundColorPicker.value, CornerRadii.EMPTY, Insets.EMPTY))
    }

    override fun validate() {

    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Int, Kolor> {
        println(generationSize)
        val newRadius = cmbCircleRadius.value.toDouble()
        this.circles =
            (1..generationSize).map { circleId ->
                Circle(newRadius).also { c ->
                    c.id = "circle-%04d".format(circleId)
                    c.fill = Color.TRANSPARENT
                    c.stroke = Color.BLACK
                }
            }


        TODO("Not yet implemented")
    }

    override fun fillOwnComponent(genome: List<Kolor>) {
        TODO("Not yet implemented")
    }

    override fun resetComponents() {
        backgroundColorPicker.value = Color.WHITE
        chkNonStop.isSelected = false;
    }
}
