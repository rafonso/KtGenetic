package rafael.ktgenetic.camouflage.fx

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

/**
 * original source: http://carl-witt.de/customized-bidirectional-bindings-in-javafx/
 * See also https://stackoverflow.com/questions/27052927/custom-bidirectional-bindings-in-javafx
 */
object BidirectionalBinding {
    /**
     * Executes updateB when propertyA is changed. Executes updateA when propertyB is changed.
     * Makes sure that no update loops are caused by mutual updates.
     *
     * @param propertyA
     * @param propertyB
     * @param updateB
     */
    fun <A, B> bindBidirectional(
        propertyA: ObservableValue<A>,
        propertyB: ObservableValue<B>,
        updateB: ChangeListener<A>,
        updateA: ChangeListener<B>
    ) {
        addFlaggedChangeListener(propertyA, updateB)
        addFlaggedChangeListener(propertyB, updateA)
    }

    fun <A, B> bindBidirectional(
        propertyA: ObservableValue<A>,
        propertyB: ObservableValue<B>,
        updateB: (A) -> Unit,
        updateA: (B) -> Unit
    ) {
        addFlaggedChangeListener(propertyA) { _, _, newValue -> updateB(newValue) }
        addFlaggedChangeListener(propertyB) { _, _, newValue -> updateA(newValue) }
    }


    /**
     * Adds a change listener to a property that will not react to changes caused (transitively) by itself (i.e. from an update call in the call tree that is a descendant of itself.)
     * @param property the property to add a change listener to
     * @param updateProperty the logic to execute when the property changes
     * @param <T> the type of the observable value
     */
    private fun <T> addFlaggedChangeListener(property: ObservableValue<T>, updateProperty: ChangeListener<T>) {
        property.addListener(object : ChangeListener<T> {
            private var alreadyCalled = false
            override fun changed(observable: ObservableValue<out T>?, oldValue: T, newValue: T) {
                if (alreadyCalled) return
                try {
                    alreadyCalled = true
                    updateProperty.changed(observable, oldValue, newValue)
                } finally {
                    alreadyCalled = false
                }
            }
        })
    }
}
