package rafael.ktgenetic.nqueens.fx

import rafael.ktgenetic.nqueens.Piece
import rafael.ktgenetic.core.processor.GeneticCrossingType
import tornadofx.*

class NBishopsApp : App(NBishopsView::class)

class NBishopsView : NPiecesView(Piece.BISHOP, "N Bishops", GeneticCrossingType.SIMPLE)
