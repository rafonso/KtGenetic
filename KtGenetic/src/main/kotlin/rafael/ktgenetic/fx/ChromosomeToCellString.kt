package rafael.ktgenetic.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableColumn
import javafx.util.Callback
import rafael.ktgenetic.Chromosome

/**
 * Converts a property inside a [Chromosome] to a String to be showed in a TableCell
 */
internal class ChromosomeToCellString<G, C : Chromosome<G>>(private val chromosomeToString: (Chromosome<G>) -> String) :
        Callback<TableColumn.CellDataFeatures<C, String>, ObservableValue<String>> {
    override fun call(param: TableColumn.CellDataFeatures<C, String>?): ObservableValue<String>  //
            = SimpleObjectProperty<String>(chromosomeToString(param!!.value))
}