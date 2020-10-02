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
import rafael.ktgenetic.fx.bindBidirectional
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.randomIntInclusive
import tornadofx.*

class CamouflageApp : App(CamouflageView::class)

class CamouflageView : GeneticView<Int, Kolor>("Camouflage", GeneticProcessorChoice.SIMPLE) {

    private fun makeIntSpinner(maxValue: Int) = spinner(0, maxValue, 0, 1, enableScroll = true).also { spn ->
        spn.editor.alignment = Pos.CENTER_RIGHT
        spn.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
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

        fun addSpinner(
            title: String,
            spinner: Spinner<Int>,
            kolorToValue: (Kolor) -> Int,
            copyColor: (Kolor, Int) -> Kolor,
            kolortoColor: (Kolor) -> Color
        ) {
            spinner.valueFactory.value = kolorToValue(backgroundKolor)
            bindBidirectional(
                backgroundKolorProperty, spinner.valueFactory.valueProperty(),
                { newKolor -> spinner.valueFactory.value = kolorToValue(newKolor) },
                { newValue -> backgroundKolor = copyColor(backgroundKolor, newValue) })

            addComponent(title, spinner).also { lbl ->
                lbl.textFill = kolortoColor(backgroundKolor)
                backgroundKolorProperty.addListener { _, _, newKolor ->
                    lbl.textFill = kolortoColor(newKolor)
                }
            }
        }


        addComponent(chkNonStop)
        cmbCircleRadius.onAction = EventHandler {
            val newRadius = cmbCircleRadius.value.toDouble()
            this.circles.forEach { it.radius = newRadius }
            reloadBackgound()
        }
        addComponent("Circles Radii", cmbCircleRadius)
        addComponent("Color Distance Calculator", cmbColorDistance, 4)

        backgroundColorPicker.value = backgroundKolor.color
        bindBidirectional(backgroundKolorProperty, backgroundColorPicker.valueProperty(),
            { newKolor -> backgroundColorPicker.value = newKolor.color },
            { newColor -> backgroundKolor = newColor.toKolor() })
        addComponent("Background Color", backgroundColorPicker)
        addSpinner("Red", spnRed, { it.r }, { k, v -> k.copy(r = v) }, { k -> Color.rgb(k.r, 0, 0) })
        addSpinner("Green", spnGreen, { it.g }, { k, v -> k.copy(g = v) }, { k -> Color.rgb(0, k.g, 0) })
        addSpinner("Blue", spnBlue, { it.b }, { k, v -> k.copy(b = v) }, { k -> Color.rgb(0, 0, k.b) })
        addComponent(Pane(), 2)

        addComponent(Pane())
        addSpinner("Hue", spnHue, { it.color.hue.toInt() },
            { k, v ->
                val c = k.color
                Color.hsb(v.toDouble(), c.saturation, c.brightness).toKolor()
            },
            { k -> Color.hsb(k.color.hue, 0.0, 0.0) }
        )
        addSpinner("Saturation (%)", spnSaturation, { (100.0 * it.color.saturation).toInt() },
            { k, v ->
                val c = k.color
                Color.hsb(c.hue, (v.toDouble() / 100.0), c.brightness).toKolor()
            },
            { k -> Color.hsb(0.0, k.color.saturation, 0.0) }
        )
        addSpinner("Brightness (%)", spnBrightness, { (100.0 * it.color.brightness).toInt() },
            { k, v ->
                val c = k.color
                Color.hsb(c.hue, c.saturation, (v.toDouble() / 100.0)).toKolor()
            },
            { k -> Color.hsb(0.0, 0.0, k.color.brightness) }
        )
        addComponent(label().also { lbl ->
            backgroundKolorProperty.addListener { _, _, k ->
                lbl.text = "HSB(%03.0f, %.2f, %.2f)".format(k.color.hue, k.color.saturation, k.color.brightness)
            }
        }, 2)

        circlesProperty.onChange { reloadBackgound() }

        val scrollPane = scrollpane(fitToWidth = true, fitToHeight = true) {
            content = pnlEnvironment
            onMouseClicked = EventHandler { evt ->
                if (evt.clickCount == 2) {
                    backgroundKolor = Kolor(
                        randomIntInclusive(MAX_COLOR_VALUE),
                        randomIntInclusive(MAX_COLOR_VALUE),
                        randomIntInclusive(MAX_COLOR_VALUE)
                    )
                }
            }
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
            circles[index].fill = kolor.color
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
