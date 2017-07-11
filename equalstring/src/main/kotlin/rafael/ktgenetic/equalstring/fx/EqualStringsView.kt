package rafael.ktgenetic.equalstring.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.util.Callback
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
        addComponent("Target", txfTarget, 3)

        val fitnessColumn = TableColumn<Word, String>("Fitness")
        fitnessColumn.prefWidth = 100.0
        fitnessColumn.cellValueFactory = WordToFitnessString()

        val wordColumn = TableColumn<Word, String>("Word")
        wordColumn.prefWidth = 500.0
        wordColumn.cellValueFactory = WordToValueString()

        wordsTable.prefWidth = Control.USE_COMPUTED_SIZE
        wordsTable.columns.addAll(fitnessColumn, wordColumn)

        root.center = wordsTable
    }

    override fun validate() {
        if (txfTarget.text.isEmpty()) {
            throw IllegalStateException("Target not defined")
        }
    }

    override fun fillOwnComponent(genome: List<Word>) {
        wordsTable.items = FXCollections.observableArrayList(genome)
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

class WordToFitnessString : Callback<TableColumn.CellDataFeatures<Word, String>, ObservableValue<String>> {
    override fun call(param: TableColumn.CellDataFeatures<Word, String>?): ObservableValue<String> = //
            SimpleObjectProperty<String>("%.3f".format(param!!.value.fitness))
}

class WordToValueString : Callback<TableColumn.CellDataFeatures<Word, String>, ObservableValue<String>> {
    override fun call(param: TableColumn.CellDataFeatures<Word, String>?): ObservableValue<String> = //
            SimpleObjectProperty<String>(String(param!!.value.content.toCharArray()))
}