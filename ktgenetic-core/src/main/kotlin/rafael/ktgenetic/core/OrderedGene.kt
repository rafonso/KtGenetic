package rafael.ktgenetic.core

/**
 * Represents an ordered gene in a genetic algorithm.
 *
 * An ordered gene is a gene that has an associated identifier (id) and a value.
 * The id is used to determine the order of the genes in a chromosome.
 *
 * @param G The type of the value of the gene.
 * @property id The identifier of the gene. This is used to determine the order of the genes in a chromosome.
 * @property value The value of the gene.
 */
data class OrderedGene<out G>(val id: Int, val value: G) {

    /**
     * Returns a hash code value for the object.
     *
     * The hash code is based on the id of the gene.
     *
     * @return A hash code value for this object.
     */
    override fun hashCode(): Int = id.hashCode()

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Two OrderedGene objects are considered equal if their ids are equal.
     *
     * @param other The reference object with which to compare.
     * @return `true` if this object is the same as the `other` argument; `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other === this) {
            return true
        }
        if (other is OrderedGene<*>) {
            return this.id == other.id
        }
        return false
    }

    /**
     * Returns a string representation of the object.
     *
     * The string representation is the string representation of the value of the gene.
     *
     * @return A string representation of the object.
     */
    override fun toString() = value.toString()

}
