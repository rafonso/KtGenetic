package rafael.ktgenetic.fx

import javafx.scene.control.Control
import javafx.scene.control.TableColumn
import javafx.scene.image.Image
import rafael.ktgenetic.core.Chromosome

/**
 * Icon for the Genetic Algorithm.
 */
val geneticIcon: Image = Image("/view/dna_helix_genetic_gene-32.png")

/**
 * Formats a [Chromosome], or one of its properties to be shown in a [TableColumn].
 *
 * @param title Column title. See [TableColumn.text]
 * @param prefWidth Column prefWidth. See [TableColumn.prefWidth]
 * @param classes Column CSS classes. See [TableColumn.styleClass]
 * @param style Column CSS style. See [TableColumn.style]
 * @param chromosomeToString Function that converts the Chromossome (ou a specific property) to a apropriataed string to be shown in the column.
 * @return A [TableColumn] showing a specific property from a [Chromosome]
 */
@JvmOverloads
fun <G, C : Chromosome<G>> chromosomeToTableColumn(
    title: String,
    prefWidth: Double = Control.USE_COMPUTED_SIZE,
    classes: List<String> = listOf(),
    style: String = "",
    chromosomeToString: (C) -> String = { c -> c.toString() }
):
        TableColumn<C, String> {
    val balanceColumn = TableColumn<C, String>(title)

    @Suppress("UNCHECKED_CAST") val converter =
        chromosomeToString as (Chromosome<G>) -> String
    balanceColumn.cellValueFactory = ChromosomeToCellString(converter)
    balanceColumn.prefWidth = prefWidth
    balanceColumn.styleClass.addAll(classes)
    balanceColumn.style = style

    return balanceColumn
}

/**
 * Formats a [Chromosome.fitness] in a String to be showed in a TableCell.
 *
 * @param prefWidth Column prefWidth. See [TableColumn.prefWidth]
 * @param classes Column CSS classes. See [TableColumn.styleClass]
 * @param style Column CSS style. See [TableColumn.style]
 * @return A [TableColumn] showing the [Chromosome.fitness]
 */
fun <G, C : Chromosome<G>> fitnessToTableColumn(
    prefWidth: Double = 50.0,
    classes: List<String> = listOf(),
    style: String = ""
):
        TableColumn<C, String> =
    chromosomeToTableColumn("Fitness", prefWidth, classes, style) { c -> "%.4f".format(c.fitness) }
