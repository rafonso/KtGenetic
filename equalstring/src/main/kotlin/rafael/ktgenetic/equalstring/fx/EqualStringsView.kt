package rafael.ktgenetic.equalstring.fx

import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.StringConverter
import rafael.ktgenetic.Environment
import rafael.ktgenetic.equalstring.EqualStringEnvironment
import rafael.ktgenetic.equalstring.StringFitnessChoice
import rafael.ktgenetic.equalstring.Word
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class EqualStringsViewApp : App(EqualStringsView::class)

class EqualStringsView : GeneticView<Char, Word>("Equal Strings", GeneticProcessorChoice.SIMPLE) {

    val wordsTable: TableView<Word> = TableView<Word>()

    private val cmbStringFitness = ComboBox<StringFitnessChoice>()

    private val txfTarget: TextField = TextField()

    init {
        cmbStringFitness.items = FXCollections.observableArrayList(StringFitnessChoice.values().toList())
        cmbStringFitness.value = StringFitnessChoice.EQUAL_CHARS
        cmbStringFitness.converter = StringFitnessChoiceConverter()
        addComponent("Fitness Method", cmbStringFitness)

        txfTarget.prefWidth = 400.0
        addComponent("Target", txfTarget, 2)

        val fitnessColumn = TableColumn<Word, Double>("Fitness")
        fitnessColumn.prefWidth = 100.0
        fitnessColumn.cellValueFactory = PropertyValueFactory<Word, Double>("fitness")

        val wordColumn = TableColumn<Word, String>("Word")
        wordColumn.prefWidth = 500.0
        wordColumn.cellValueFactory = PropertyValueFactory<Word, String>("content")

        wordsTable.prefWidth = Control.USE_COMPUTED_SIZE
        wordsTable.columns.addAll(fitnessColumn, wordColumn)

        val pane = ScrollPane(wordsTable)
        pane.vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS

        wordsTable.items = FXCollections.observableArrayList((1..100).map { Word("TESTEEEEEEEEEEEEE") })

        root.center = pane
    }

    override fun validate() {
        if (txfTarget.text.isEmpty()) {
            throw IllegalStateException("Target not defined")
        }
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