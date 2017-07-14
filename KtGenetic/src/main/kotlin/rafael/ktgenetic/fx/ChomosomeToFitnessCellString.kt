package rafael.ktgenetic.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableColumn
import javafx.util.Callback
import rafael.ktgenetic.Chromosome

class ChomosomeToFitnessCellString<C: Chromosome<*>> : Callback<TableColumn.CellDataFeatures<C, String>, ObservableValue<String>> {
    override fun call(param: TableColumn.CellDataFeatures<C, String>?): ObservableValue<String> = //
            SimpleObjectProperty<String>("%.3f".format(param!!.value.fitness))
}