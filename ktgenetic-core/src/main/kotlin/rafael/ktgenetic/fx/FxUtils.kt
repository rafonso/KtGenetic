package rafael.ktgenetic.fx

import javafx.scene.control.Control
import javafx.scene.control.TableColumn
import rafael.ktgenetic.Chromosome

fun <G, C : Chromosome<G>> chromosomeToTableColumn(title: String,
                                                   chromosomeToString: (C) -> String,
                                                   prefWidth: Double = Control.USE_COMPUTED_SIZE,
                                                   classes: List<String> = listOf(),
                                                   style: String = ""):
        TableColumn<C, String> {
    val balanceColumn = TableColumn<C, String>(title)

    @Suppress("UNCHECKED_CAST") val converter = chromosomeToString as (@kotlin.Suppress("UNCHECKED_CAST") Chromosome<G>) -> String
    balanceColumn.cellValueFactory = ChromosomeToCellString(converter)
    balanceColumn.prefWidth = prefWidth
    balanceColumn.styleClass.addAll(classes)
    balanceColumn.style = style

    return balanceColumn
}

/**
 * Converts a property inside a [Chromosome.fitness] to a String to be showed in a TableCell
 */
fun <G, C : Chromosome<G>> fitnessToTableColumn(prefWidth: Double = 50.0,
                                                classes: List<String> = listOf(),
                                                style: String = ""):
        TableColumn<C, String> =
        chromosomeToTableColumn("Fitness",
                { c -> "%.4f".format(c.fitness) },
                prefWidth,
                classes,
                style)