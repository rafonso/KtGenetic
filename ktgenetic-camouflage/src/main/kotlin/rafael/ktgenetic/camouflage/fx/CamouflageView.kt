package rafael.ktgenetic.camouflage.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.camouflage.CamouflageEnvironment
import rafael.ktgenetic.camouflage.Kolor
import rafael.ktgenetic.camouflage.MAX_COLOR_VALUE
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

fun Kolor.toColor(): Color = Color.rgb(this.r, this.g, this.b)

fun Color.toKolor() = Kolor(
    (this.red * MAX_COLOR_VALUE).toInt(),
    (this.green * MAX_COLOR_VALUE).toInt(),
    (this.blue * MAX_COLOR_VALUE).toInt()
)

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

    private val chkNonStop = checkbox("Run non Stop").also { chk ->
        chk.tooltip = Tooltip("When checked, it will run indefinitely, ignoring Generations Combo")
    }

    private val cmbCircleRadius = combobox<Int> {
        items = observableListOf(10, 20, 30, 40, 50)
        value = 10
    }

    private var backgroundColorPickerParent: Node

    // OUTPUT COMPONENTS

    private val pnlEnvironment = flowpane {
        padding = insets(5)
        vgap = 5.0
        hgap = 5.0
    }

    // OTHER COMPONENTS

    private val circlesProperty = SimpleObjectProperty(listOf<Circle>())
    private var circles by circlesProperty

    private var backgroundColorToEnvironmentListener: ChangeListener<Color>? = null

    init {
        backgroundColorPicker.onAction = EventHandler { changeBackground() }

        addComponent("Background Color", backgroundColorPicker)
        backgroundColorPickerParent = backgroundColorPicker.parent
        addComponent(chkNonStop)

        cmbCircleRadius.onAction = EventHandler {
            val newRadius = cmbCircleRadius.value.toDouble()
            this.circles.forEach { it.radius = newRadius }
            reloadBackgound()
        }
        addComponent("Circles Radi", cmbCircleRadius)

        circlesProperty.onChange { reloadBackgound() }

        val scrollPane = scrollpane(fitToWidth = true, fitToHeight = true) {
            content = pnlEnvironment
        }
        root.center = scrollPane

        changeBackground()
    }

    override fun alwaysEnabledComponents(): List<Node> = listOf(backgroundColorPickerParent)

    private fun reloadBackgound() {
        pnlEnvironment.clear()
        this.circles.forEach { pnlEnvironment.add(it) }
    }

    private fun changeBackground() {
        pnlEnvironment.background =
            Background(BackgroundFill(backgroundColorPicker.value, CornerRadii.EMPTY, Insets.EMPTY))
    }

    override fun validate() {
// Do nothing
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Int, Kolor> {
        val newRadius = cmbCircleRadius.value.toDouble()
        this.circles =
            (1..generationSize).map { circleId ->
                Circle(newRadius).also { c ->
                    c.id = "circle-%04d".format(circleId)
                    c.fill = Color.TRANSPARENT
                    c.stroke = Color.BLACK
                }
            }

        val maxGen = if (chkNonStop.isSelected) Integer.MAX_VALUE else maxGenerations
        return CamouflageEnvironment(
            backgroundColorPicker.value.toKolor(),
            maxGen,
            generationSize,
            mutationFactor
        ).also { env ->
            backgroundColorToEnvironmentListener = ChangeListener { _, _, newColor ->
                env.backgroundColor = newColor.toKolor()
            }
            backgroundColorPicker.valueProperty().addListener(backgroundColorToEnvironmentListener)
        }
    }

    override fun fillOwnComponent(genome: List<Kolor>) {
        genome.forEachIndexed { index, kolor ->
            circles[index].fill = kolor.toColor()
            circles[index].tooltip(
                "Color: ${circles[index].fill}, Fitness: ${kolor.fitness}"
            )
        }
    }

    override fun resetComponents() {
        backgroundColorPicker.value = Color.WHITE
        chkNonStop.isSelected = false
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        super.onEvent(event)
        if (event.eventType.ended) {
            backgroundColorPicker.valueProperty().removeListener(backgroundColorToEnvironmentListener)
            backgroundColorToEnvironmentListener = null
        }
    }

}
