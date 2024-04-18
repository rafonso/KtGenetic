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
import rafael.ktgenetic.core.events.GenerationEvent
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.core.selection.SelectionOperatorChoice

/**
 * Class responsible for handling the inputs of a genetic algorithm.
 *
 * @property pnlInput The input panel.
 * @property pnlButtons The buttons panel.
 * @property cmbGenerations The combo box for selecting the number of generations.
 * @property cmbPopulation The combo box for selecting the population size.
 * @property cmbMutationFactor The combo box for selecting the mutation factor.
 * @property cmbSelectionOperator The combo box for selecting the selection operator.
 * @property chbElitism The check box for enabling or disabling elitism.
 * @property chbRepeatedValues The check box for enabling or disabling repeated values.
 * @property btnStop The stop button.
 * @property btnReset The reset button.
 * @property btnStart The start button.
 */
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

        cmbSelectionOperator.items = FXCollections.observableArrayList(SelectionOperatorChoice.entries)
        cmbSelectionOperator.value = SelectionOperatorChoice.TRUNCATE
        cmbSelectionOperator.converter = SelectionOperatorConverter()
    }

    /**
     * Adds a component to the input panel.
     *
     * @param title The title of the component.
     * @param component The component to add.
     * @param colspan The number of columns the component should span.
     * @return The label of the added component.
     */
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

    /**
     * Disables or enables the input components.
     *
     * @param disable Whether to disable the input components.
     */
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

    /**
     * Resets the view to its initial state.
     */
    fun reset() {
        cmbSelectionOperator.selectionModel.selectFirst()
        cmbGenerations.value = 100
        cmbMutationFactor.value = 0.1
        cmbPopulation.value = 100
    }

    /**
     * Handles a change in the generation event.
     *
     * @param observable The observable value that has changed.
     * @param oldValue The old value of the generation event.
     * @param newValue The new value of the generation event.
     */
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

/**
 * Converter for the selection operator choice.
 */
class SelectionOperatorConverter : StringConverter<SelectionOperatorChoice>() {

    /**
     * Converts a selection operator choice to a string.
     *
     * @param choice The selection operator choice to convert.
     * @return The string representation of the selection operator choice.
     */
    override fun toString(choice: SelectionOperatorChoice?): String = choice!!.description

    /**
     * Converts a string to a selection operator choice.
     *
     * @param string The string to convert.
     * @return The selection operator choice represented by the string.
     */
    override fun fromString(string: String?): SelectionOperatorChoice =
        SelectionOperatorChoice.entries.first { it.description == string }

}



