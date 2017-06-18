package rafael.ktgenetic

data class ChromosomePieces<out G>(val left: List<G>, val core: List<G>, val right: List<G>) {

    val join: List<G> by lazy({
        left + core + right
    })

}