package rafael.ktgenetic.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableColumn
import javafx.util.Callback
import rafael.ktgenetic.core.Chromosome

/**
 * Class responsible for converting a property inside a [Chromosome] to a String to be displayed in a TableCell.
 *
 * @property chromosomeToString A function that converts a Chromosome (or a specific property) to an appropriate string to be shown in the column.
 * @constructor Creates a new ChromosomeToCellString with the given chromosomeToString function.
 */
internal class ChromosomeToCellString<G, C : Chromosome<G>>(private val chromosomeToString: (Chromosome<G>) -> String) :
    Callback<TableColumn.CellDataFeatures<C, String>, ObservableValue<String>> {

    /**
     * Converts a property inside a [Chromosome] to a String to be displayed in a TableCell.
     *
     * @param param The cell data features.
     * @return An ObservableValue containing the string representation of the chromosome.
     */
    override fun call(param: TableColumn.CellDataFeatures<C, String>?): ObservableValue<String>  //
            = SimpleObjectProperty(chromosomeToString(param!!.value))

}
