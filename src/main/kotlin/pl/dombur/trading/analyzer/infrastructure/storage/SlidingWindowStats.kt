package pl.dombur.trading.analyzer.infrastructure.storage

import kotlin.math.max
import kotlin.math.min

data class SlidingWindowStats(
    val windowSize: Int,
    var min: Double = Double.MAX_VALUE,
    var max: Double = Double.MIN_VALUE,
    var sum: Double = 0.0,
    var sumOfSquares: Double = 0.0,
    var last: Double = 0.0,
    val values: ArrayDeque<Double> = ArrayDeque(),
) {
    fun addValue(value: Double) {
        values.addLast(value)
        sum += value
        sumOfSquares += value * value
        min = min(min, value)
        max = max(max, value)
        last = value
    }

    fun removeFirstRange(rangeToRemove: Int) {
        val removedBatch =
            (1..rangeToRemove).map {
                val removed = values.removeFirst()
                sum -= removed
                sumOfSquares -= removed * removed
                removed
            }

        if (min in removedBatch && values.isNotEmpty()) {
            min = values.min()
        }

        if (max in removedBatch && values.isNotEmpty()) {
            max = values.max()
        }
    }
}
