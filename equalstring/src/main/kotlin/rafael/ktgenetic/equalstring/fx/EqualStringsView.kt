package rafael.ktgenetic.equalstring.fx

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
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
    private val cmbStringFitness = ComboBox<StringFitnessChoice>()

    private val txfTarget: TextField = TextField()

    init {
        cmbStringFitness.items = FXCollections.observableArrayList(StringFitnessChoice.values().toList())
        cmbStringFitness.value = StringFitnessChoice.EQUAL_CHARS
        cmbStringFitness.converter = StringFitnessChoiceConverter()
        addComponent("Fitness Method", cmbStringFitness)

        txfTarget.prefWidth = 400.0
        addComponent("Target", txfTarget, 2)
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