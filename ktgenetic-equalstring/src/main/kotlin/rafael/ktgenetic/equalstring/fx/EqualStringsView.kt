package rafael.ktgenetic.equalstring.fx

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.Control
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.util.StringConverter
import rafael.ktgenetic.Environment
import rafael.ktgenetic.equalstring.EqualStringEnvironment
import rafael.ktgenetic.equalstring.StringFitnessChoice
import rafael.ktgenetic.equalstring.Word
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.chromosomeToTableColumn
import rafael.ktgenetic.fx.fitnessToTableColumn
import rafael.ktgenetic.processor.GeneticCrossingType
import tornadofx.*

class EqualStringsViewApp : App(EqualStringsView::class)

class EqualStringsView : GeneticView<Char, Word>("Equal Strings", GeneticCrossingType.SIMPLE) {

    // INPUT COMPONENTS

    private val cmbStringFitness: ComboBox<StringFitnessChoice> = combobox {
        items = FXCollections.observableArrayList(StringFitnessChoice.values().toList())
        value = StringFitnessChoice.EQUAL_CHARS
        converter = StringFitnessChoiceConverter()
    }

    private val txfTarget: TextField = textfield {
        prefWidth = 400.0
    }

    // OUTPUT COMPONENTS

    private val wordsTable: TableView<Word> = tableview {
        val classes = listOf("mono")
        val fitnessColumn = fitnessToTableColumn<Char, Word>(50.0, classes)
        val wordColumn = chromosomeToTableColumn<Char, Word>("Word", 500.0, classes) { (content) ->
            String(content.toCharArray())
        }

        prefWidth = Control.USE_COMPUTED_SIZE
        columns.addAll(fitnessColumn, wordColumn)
    }

    init {
        addComponent("Fitness Method", cmbStringFitness)
        addComponent("Target", txfTarget, 3)

        root.center = wordsTable
    }

    override fun validate() {
        check(txfTarget.text.isNotEmpty()) { "Target not defined" }
    }

    override fun fillOwnComponent(genome: List<Word>) {
        wordsTable.items = FXCollections.observableArrayList(genome)
    }

    override fun resetComponents() {
        cmbStringFitness.selectionModel.selectFirst()
        txfTarget.text = ""
        wordsTable.items = FXCollections.emptyObservableList()
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Char, Word> =
            EqualStringEnvironment(
                    txfTarget.text, //
                    cmbStringFitness.value.fitness,
                    maxGenerations,
                    generationSize,
                    mutationFactor)

}

class StringFitnessChoiceConverter : StringConverter<StringFitnessChoice>() {

    override fun toString(choice: StringFitnessChoice?): String = choice!!.description

    override fun fromString(string: String?): StringFitnessChoice = StringFitnessChoice.values().first { it.description == string }

}
