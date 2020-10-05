package rafael.ktgenetic.nqueens.fx

import rafael.ktgenetic.nqueens.Piece
import rafael.ktgenetic.processor.GeneticCrossingType
import tornadofx.*

class NQueensApp : App(NQueensView::class)

class NQueensView : NPiecesView(Piece.QUEEN, "N Queens", GeneticCrossingType.ORDERED)
