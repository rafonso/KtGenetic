package rafael.ktgenetic.track

import rafael.ktgenetic.Chromosome

data class Point(val hPos: Int, val vPos: Int)

data class Path(override val content: List<Direction>) : Chromosome<Direction>() {

    private lateinit var _status: PathStatus

    val status: PathStatus
        get() = _status

    fun calculatePath(hLimit: Int, vLimit: Int) {

    }

}