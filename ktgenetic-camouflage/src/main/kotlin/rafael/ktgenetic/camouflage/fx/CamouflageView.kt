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
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.camouflage.*
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.bindBidirectional
import rafael.ktgenetic.processor.GeneticCrossingType
import rafael.ktgenetic.randomIntInclusive
import tornadofx.*

class CamouflageApp : App(CamouflageView::class)

class CamouflageView : GeneticView<Int, Kolor>("Camouflage", GeneticCrossingType.SIMPLE) {

    private fun makeIntSpinner() = spinner(0, MAX_COLOR_VALUE, 0, 1, enableScroll = true).also { spn ->
        spn.editor.alignment = Pos.CENTER_RIGHT
        spn.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
    }

    private val changeColorActions = mapOf<String, (Color) -> Color>(
        "Brighter" to { it.brighter() },
        "Darker" to { it.darker() },
        "Desaturate" to { it.desaturate() },
        "Grayscale" to { it.grayscale() },
        "Invert" to { it.invert() },
        "Staturate" to { it.saturate() }
    )

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

    private val spnRed = makeIntSpinner()

    private val spnGreen = makeIntSpinner()

    private val spnBlue = makeIntSpinner()

    private val btnChangeBackground = button("Change Background to ") {
        onAction = EventHandler { changeBackground1() }
    }

    private val cmbChangeBackground = combobox(values = changeColorActions.keys.toList()) {
        value = changeColorActions.keys.first()
    }


    // OUTPUT COMPONENTS

    private val pnlEnvironment = flowpane {
        padding = insets(5)
        vgap = 5.0
        hgap = 5.0
    }

    // OTHER COMPONENTS

    private val circlesProperty = SimpleObjectProperty(listOf<CircleNode>())
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
        addComponent("Circles Radii", cmbCircleRadius)
        addComponent("Color Distance Calculator", cmbColorDistance, 4)

        backgroundColorPicker.value = backgroundKolor.color
        bindBidirectional(backgroundKolorProperty, backgroundColorPicker.valueProperty(),
            { newKolor -> backgroundColorPicker.value = newKolor.color },
            { newColor -> backgroundKolor = newColor.toKolor() })
        addComponent("Background Color", backgroundColorPicker)
        addSpinner("Red"  , spnRed  , { it.r }, { k, v -> k.copy(r = v) }, { k -> Color.rgb(k.r, 0, 0) })
        addSpinner("Green", spnGreen, { it.g }, { k, v -> k.copy(g = v) }, { k -> Color.rgb(0, k.g, 0) })
        addSpinner("Blue" , spnBlue , { it.b }, { k, v -> k.copy(b = v) }, { k -> Color.rgb(0, 0, k.b) })
        addComponent(FlowPane(btnChangeBackground, cmbChangeBackground).also {
            it.hgap = 5.0
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
            btnChangeBackground.parent,
        ).map { c -> c.parent }

    private fun reloadBackgound() {
        pnlEnvironment.clear()
        this.circles.forEach { pnlEnvironment.add(it) }
    }

    private fun changeBackground() {
        pnlEnvironment.background =
            Background(BackgroundFill(backgroundColorPicker.value, CornerRadii.EMPTY, Insets.EMPTY))
    }

    private fun changeBackground1() {
        this.backgroundKolor =
            changeColorActions[cmbChangeBackground.value]?.let { it(this.backgroundKolor.color) }!!.toKolor()
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
                CircleNode(newRadius, circleId)
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
            circles[index].kolor = kolor
        }
    }

    override fun resetComponents() {
        chkNonStop.isSelected = false
        cmbCircleRadius.value = 10
        cmbColorDistance.value = KolorDistance.RGB
        backgroundKolor = WHITE
        pnlEnvironment.clear()
        circles = emptyList()
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        super.onEvent(event)
        if (event.eventType.ended) {
            backgroundKolorProperty.removeListener(backgroundKolorToEnvironmentListener)
            backgroundKolorToEnvironmentListener = null
        }
    }

}
