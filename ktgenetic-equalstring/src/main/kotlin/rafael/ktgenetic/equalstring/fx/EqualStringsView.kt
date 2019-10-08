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
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class EqualStringsViewApp : App(EqualStringsView::class)

class EqualStringsView : GeneticView<Char, Word>("Equal Strings", GeneticProcessorChoice.SIMPLE) {

    // INPUT COMPONENTS

    private val cmbStringFitness = ComboBox<StringFitnessChoice>()

    private val txfTarget: TextField = TextField()

    // OUTPUT COMPONENTS

    private val wordsTable: TableView<Word> = TableView()

    init {
        cmbStringFitness.items = FXCollections.observableArrayList(StringFitnessChoice.values().toList())
        cmbStringFitness.value = StringFitnessChoice.EQUAL_CHARS
        cmbStringFitness.converter = StringFitnessChoiceConverter()
        addComponent("Fitness Method", cmbStringFitness)

        txfTarget.prefWidth = 400.0
        addComponent("Target", txfTarget, 3)

        val classes = listOf("mono")

        val fitnessColumn = fitnessToTableColumn<Char, Word>(50.0, classes)

        val wordColumn = chromosomeToTableColumn<Char, Word>("Word",
                500.0,
                classes,
                chromosomeToString = { (content) -> String(content.toCharArray()) })

        wordsTable.prefWidth = Control.USE_COMPUTED_SIZE
        wordsTable.columns.addAll(fitnessColumn, wordColumn)

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
