package rafael.ktgenetic.nqueens.fx

import rafael.ktgenetic.nqueens.Piece
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class NBishopsApp : App(NBishopsView::class)

class NBishopsView : NPiecesView(Piece.BISHOP, "N Bishops", GeneticProcessorChoice.SIMPLE)