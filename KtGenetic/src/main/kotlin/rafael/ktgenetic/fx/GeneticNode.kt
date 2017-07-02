package rafael.ktgenetic.fx

import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.FlowPane
import tornadofx.*

class GeneticNode(private val description: String, node: Node) : FlowPane(Orientation.VERTICAL) {

    init {
        this.add(Label(description))
        this.add(node)
        this.styleClass.add("panel-control")
    }

}
