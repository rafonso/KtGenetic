package rafael.ktgenetic.camouflage.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.event.Event
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.Tooltip
import javafx.scene.input.*
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.camouflage.*
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

    private fun makeIntSpinner(maxValue: Int) = spinner(0, maxValue, 0, 1, enableScroll = true).also { spn ->
        spn.editor.alignment = Pos.CENTER_RIGHT
        spn.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
    }

    private fun confgureIntSpinner(
        spinner: Spinner<Int>,
        kolorToValue: (Kolor) -> Int,
        copyColor: (Kolor, Int) -> Kolor
    ) {
        spinner.valueFactory.value = kolorToValue(backgroundKolor)
        BidirectionalBinding.bindBidirectional(
            backgroundKolorProperty, spinner.valueFactory.valueProperty(),
            { _, _, newKolor -> spinner.valueFactory.value = kolorToValue(newKolor) },
            { _, _, newValue -> backgroundKolor = copyColor(backgroundKolor, newValue) })
    }

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

    private val cmbColorDistance = combobox<KolorDistance> {
        items = observableListOf(KolorDistance.values().toList())
        value = KolorDistance.RGB
    }

    private val spnRed = makeIntSpinner(MAX_COLOR_VALUE)

    private val spnGreen = makeIntSpinner(MAX_COLOR_VALUE)

    private val spnBlue = makeIntSpinner(MAX_COLOR_VALUE)

    private val spnHue = makeIntSpinner(360)

    private val spnSaturation = makeIntSpinner(100)

    private val spnBrightness = makeIntSpinner(100)

    // OUTPUT COMPONENTS

    private val pnlEnvironment = flowpane {
        padding = insets(5)
        vgap = 5.0
        hgap = 5.0
    }

    // OTHER COMPONENTS

    private val circlesProperty = SimpleObjectProperty(listOf<Circle>())
    private var circles by circlesProperty

    private var backgroundKolorToEnvironmentListener: ChangeListener<Kolor>? = null

    private val backgroundKolorProperty = SimpleObjectProperty(WHITE)
    private var backgroundKolor by backgroundKolorProperty


    init {
        addComponent(chkNonStop)
        cmbCircleRadius.onAction = EventHandler {
            val newRadius = cmbCircleRadius.value.toDouble()
            this.circles.forEach { it.radius = newRadius }
            reloadBackgound()
        }
        addComponent("Circles Radii", cmbCircleRadius)
        addComponent("Color Distance Calculator", cmbColorDistance, 4)

        backgroundColorPicker.value = backgroundKolor.color
        BidirectionalBinding.bindBidirectional(backgroundKolorProperty, backgroundColorPicker.valueProperty(),
            { _, _, newKolor -> backgroundColorPicker.value = newKolor.toColor() },
            { _, _, newColor -> backgroundKolor = newColor.toKolor() })
        addComponent("Background Color", backgroundColorPicker)

        confgureIntSpinner(spnRed, { k -> k.r }, { k, v -> k.copy(r = v) })
        addComponent("Red", spnRed)

        confgureIntSpinner(spnGreen, { k -> k.g }, { k, v -> k.copy(g = v) })
        addComponent("Green", spnGreen)

        confgureIntSpinner(spnBlue, { k -> k.b }, { k, v -> k.copy(b = v) })
        addComponent("Blue", spnBlue, 3)

        addComponent(Pane())

        confgureIntSpinner(spnHue, { k -> k.color.hue.toInt() }, { k, v ->
            val c = k.color
            Color.hsb(v.toDouble(), c.saturation, c.brightness).toKolor()
        })
        addComponent("Hue", spnHue)

        confgureIntSpinner(spnSaturation, { k -> (100.0 * k.color.saturation).toInt() }, { k, v ->
            val c = k.color
            Color.hsb(c.hue, (v.toDouble() / 100.0), c.brightness).toKolor()
        })
        addComponent("Saturation (%)", spnSaturation)

        confgureIntSpinner(spnBrightness, { k -> (100.0 * k.color.brightness).toInt() }, { k, v ->
            val c = k.color
            Color.hsb(c.hue, c.saturation, (v.toDouble() / 100.0)).toKolor()
        })
        addComponent("Brightness (%)", spnBrightness)

        circlesProperty.onChange { reloadBackgound() }

        val scrollPane = scrollpane(fitToWidth = true, fitToHeight = true) {
            content = pnlEnvironment
        }
        root.center = scrollPane

        backgroundKolorProperty.onChange { changeBackground() }
        changeBackground()
    }

    override fun alwaysEnabledComponents(): List<Node> =
        listOf(
            backgroundColorPicker,
            spnRed,
            spnGreen,
            spnBlue,
            spnHue,
            spnSaturation,
            spnBrightness
        ).map { c -> c.parent }

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
            backgroundKolor,
            cmbColorDistance.value,
            maxGen,
            generationSize,
            mutationFactor
        ).also { env ->
            backgroundKolorToEnvironmentListener = ChangeListener { _, _, newKolor ->
                env.backgroundColor = newKolor
            }
            backgroundKolorProperty.addListener(backgroundKolorToEnvironmentListener)
        }
    }

    override fun fillOwnComponent(genome: List<Kolor>) {
        genome.forEachIndexed { index, kolor ->
            circles[index].fill = kolor.toColor()
            circles[index].strokeWidth = 1 + kolor.fitness
            circles[index].tooltip("Color: %s, Fitness: %.5f".format(circles[index].fill, kolor.fitness))
        }
    }

    override fun resetComponents() {
        backgroundKolor = WHITE
        chkNonStop.isSelected = false
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        super.onEvent(event)
        if (event.eventType.ended) {
            backgroundKolorProperty.removeListener(backgroundKolorToEnvironmentListener)
            backgroundKolorToEnvironmentListener = null
        }
    }

}
