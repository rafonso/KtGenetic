package rafael.ktgenetic.fx

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import rafael.ktgenetic.GenerationEvent
import rafael.ktgenetic.TypeProcessorEvent
import rafael.ktgenetic.selection.SelectionOperatorChoice

internal class InputsView(
    // @formatter:off
    private val pnlInput                : GridPane                          ,
    private val pnlButtons              : GridPane                          ,
    private val cmbGenerations          : ComboBox<Int>                     ,
    private val cmbPopulation           : ComboBox<Int>                     ,
    private val cmbMutationFactor       : ComboBox<Double>                  ,
    private val cmbSelectionOperator    : ComboBox<SelectionOperatorChoice> ,
    private val chbElitism              : CheckBox                          ,
    private val chbRepeatedValues       : CheckBox                          ,
    private val btnStop                 : Button                            ,
    private val btnReset                : Button                            ,
    private val btnStart                : Button
    // @formatter:on
) : ChangeListener<GenerationEvent> {

    private val values: ObservableList<Int> =
        FXCollections.observableArrayList(
            1,
            10,
            50,
            100,
            200,
            300,
            400,
            500,
            600,
            700,
            800,
            900,
            1000,
            1500,
            2000,
            2500,
            3000,
            3500,
            4000
        )

    private val mutationFactors: ObservableList<Double> =
        FXCollections.observableArrayList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)

    private val maxColumns = 6

    private var lastRow = 1
    private var lastColumn = 0

    val generations: Int
        get() = cmbGenerations.value

    val population: Int
        get() = cmbPopulation.value

    val mutationFactor: Double
        get() = cmbMutationFactor.value

    val selectionOperator: SelectionOperatorChoice
        get() = cmbSelectionOperator.value

    val hasElitism: Boolean
        get() = chbElitism.isSelected

    val hasRepeatedValues: Boolean
        get() = chbRepeatedValues.isSelected

    internal var enabledComponents: List<Node> = emptyList()

    init {
        cmbGenerations.items = values
        cmbGenerations.value = 100

        cmbPopulation.items = values
        cmbPopulation.value = 100

        cmbMutationFactor.items = mutationFactors
        cmbMutationFactor.value = 0.1

        cmbSelectionOperator.items = FXCollections.observableArrayList(SelectionOperatorChoice.values().toList())
        cmbSelectionOperator.value = SelectionOperatorChoice.TRUNCATE
        cmbSelectionOperator.converter = SelectionOperatorConverter()
    }

    internal fun addComponent(title: String, component: Node, colspan: Int = 1): Label {
        assert(colspan <= maxColumns) { "Colspan must be at least $maxColumns. It was $colspan" }

        val panel = FlowPane(Orientation.VERTICAL)
        panel.styleClass.add("panel-control")
        val lblTitle = Label(title)
        panel.children.add(lblTitle)
        panel.children.add(component)

        if (lastColumn + colspan > maxColumns) {
            lastRow += 1
            lastColumn = 0
        }

        pnlInput.add(panel, lastColumn, lastRow, colspan, 1)
        lastColumn += colspan

        return lblTitle
    }

    internal fun disableInputComponents(disable: Boolean) {
        btnStart.isDisable = disable
        btnReset.isDisable = disable
        btnStop.isDisable = !disable
        pnlInput.children
            .filtered { it != pnlButtons }
            .filtered {
                !enabledComponents.contains(it)
            }
            .forEach { it.isDisable = disable }
    }

    fun reset() {
        cmbSelectionOperator.selectionModel.selectFirst()
        cmbGenerations.value = 100
        cmbMutationFactor.value = 0.1
        cmbPopulation.value = 100
    }

    override fun changed(
        observable: ObservableValue<out GenerationEvent>?,
        oldValue: GenerationEvent?,
        newValue: GenerationEvent?
    ) {
        val event = newValue!!
        when {
            event.eventType == TypeProcessorEvent.STARTING ->
                disableInputComponents(true)
            event.eventType == TypeProcessorEvent.ERROR -> {
                btnStop.isDisable = true
                btnReset.isDisable = false
            }
            event.eventType.ended ->
                disableInputComponents(false)
        }
    }

}

class SelectionOperatorConverter : StringConverter<SelectionOperatorChoice>() {

    override fun toString(choice: SelectionOperatorChoice?): String = choice!!.description

    override fun fromString(string: String?): SelectionOperatorChoice =
        SelectionOperatorChoice.values().first { it.description == string }

}



