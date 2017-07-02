package rafael.ktgenetic.fx

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class GeneticViewTestApp : App(GeneticViewTest::class)

class GeneticViewTest : GeneticView("TESTE", GeneticProcessorChoice.SIMPLE) {

    private val txfValor = TextField()
    private val cmbValores = ComboBox<Int>(FXCollections.observableArrayList(1, 2, 3, 4))
/*
    private val node: GeneticNode by lazy {
        GeneticNode("ALFA:", TextField("TESTE"))

    }
*/
    init {
        val nodeText = GeneticNode("Text:", txfValor)
        pnlInput.add(nodeText,0, 1)
        val nodeCombo = GeneticNode("Value:", cmbValores)
        pnlInput.add(nodeCombo, 1, 1)
    }


}
