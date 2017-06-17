package rafael.ktgenetic

data class OrderedGene<out G>(val id: Int, val value: G) {

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other === this) {
            return true
        }
        if (other is OrderedGene<*>) {
//            if(other.value.javaClass.?equals(this.value.javaClass))
            return this.id == other.id
        }
        return false
    }

    override fun toString() = value.toString()

}