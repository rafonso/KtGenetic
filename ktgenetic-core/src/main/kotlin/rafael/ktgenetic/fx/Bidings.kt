package rafael.ktgenetic.fx

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

/**
 * Executes [aListenerToUpdateB] when [propertyA] is changed. Executes [bListenerToUpdateA] when [propertyB] is changed.
 * Makes sure that no update loops are caused by mutual updates.
 *
 * Original source: http://carl-witt.de/customized-bidirectional-bindings-in-javafx/
 * See also https://stackoverflow.com/questions/27052927/custom-bidirectional-bindings-in-javafx
 *
 * @param propertyA [ObservableValue] of type [A] to be bound.
 * @param propertyB [ObservableValue] of type [B] to be bound.
 * @param aListenerToUpdateB [ChangeListener] of [propertyA] to update [propertyB]
 * @param bListenerToUpdateA [ChangeListener] of [propertyB] to update [propertyA]
 * @param A Type of [propertyA]
 * @param B Type of [propertyB]
 */
fun <A, B> bindBidirectional(
    propertyA: ObservableValue<A>,
    propertyB: ObservableValue<B>,
    aListenerToUpdateB: ChangeListener<A>,
    bListenerToUpdateA: ChangeListener<B>
) {
    addFlaggedChangeListener(propertyA, aListenerToUpdateB)
    addFlaggedChangeListener(propertyB, bListenerToUpdateA)
}

/**
 * Creates a mutual bound between 2 [ObservableValue]s, making sure that no update loops are caused by mutual updates.
 *
 * @param propertyA [ObservableValue] of type [A] to be bound.
 * @param propertyB [ObservableValue] of type [B] to be bound.
 * @param newAFunctionToUpdateB Function that receives the new value of [propertyA] to update [propertyB]
 * @param newBFunctionToUpdateA Function that receives the new value of [propertyB] to update [propertyA]
 * @param A Type of [propertyA]
 * @param B Type of [propertyB]
 * @see [bindBidirectional]
 */
fun <A, B> bindBidirectional(
    propertyA: ObservableValue<A>,
    propertyB: ObservableValue<B>,
    newAFunctionToUpdateB: (A) -> Unit,
    newBFunctionToUpdateA: (B) -> Unit
) {
    bindBidirectional(
        propertyA,
        propertyB,
        { _, _, newValue -> newAFunctionToUpdateB(newValue) },
        { _, _, newValue -> newBFunctionToUpdateA(newValue) }
    )
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
