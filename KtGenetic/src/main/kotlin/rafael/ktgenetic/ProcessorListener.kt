package rafael.ktgenetic

// https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
interface ProcessorListener {
    fun onEvent(event: ProcessorEvent)
}