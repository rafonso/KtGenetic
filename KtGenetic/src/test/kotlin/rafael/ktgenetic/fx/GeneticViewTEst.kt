package rafael.ktgenetic.fx

import javafx.collections.FXCollections
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class GeneticViewTestApp : App(GeneticViewTest::class)

class GeneticViewTest : GeneticView("TESTE", GeneticProcessorChoice.SIMPLE) {

    private val txfValor = TextField()
    private val cmbValores = ComboBox<Int>(FXCollections.observableArrayList(1, 2, 3, 4))

    init {
        txfValor.prefWidth = 400.0
        addComponent("Text", txfValor, 2)
        addComponent("Value:", cmbValores)
        addComponent("Jumping row", TextField(), 4)
        addComponent("It should be at end of row", CheckBox())
    }


}
