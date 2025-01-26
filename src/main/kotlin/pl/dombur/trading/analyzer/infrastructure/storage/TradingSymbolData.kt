package pl.dombur.trading.analyzer.infrastructure.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsModel
import pl.dombur.trading.analyzer.domain.exception.NotEnoughTradingDataException
import pl.dombur.trading.analyzer.domain.exception.QueryOutOfRangeException
import kotlin.math.pow

data class TradingSymbolData(
    private val symbol: String,
) {
    companion object {
        private const val MIN_DATA_POINTS_EXPONENT = 1
        private const val MAX_DATA_POINTS_EXPONENT = 8
    }

    private val windows: Map<Int, SlidingWindowStats> =
        (MIN_DATA_POINTS_EXPONENT..MAX_DATA_POINTS_EXPONENT)
            .associateWith { SlidingWindowStats(it) }

    fun addValues(newValues: List<Double>) =
        runBlocking {
            windows
                .map { (_, stats) ->
                    async(Dispatchers.Default) {
                        addValuesToWindow(newValues, stats)
                    }
                }.awaitAll()
        }

    private fun addValuesToWindow(
        newValues: List<Double>,
        stats: SlidingWindowStats,
    ) {
        val maxSize = 10.0.pow(stats.windowSize).toInt()
        newValues
            .takeLast(maxSize)
            .forEach { value -> stats.addValue(value) }

        if (stats.values.size > maxSize) {
            val rangeToRemove = stats.values.size - maxSize
            stats.removeFirstRange(rangeToRemove)
        }
    }

    fun getStats(k: Int): TradingSymbolStatsModel {
        val stats = windows[k] ?: throw QueryOutOfRangeException(k)

        val count = stats.values.size
        if (count < 10.0.pow(k).toInt()) {
            throw NotEnoughTradingDataException(symbol)
        }

        val avg = stats.sum / count
        val variance = (stats.sumOfSquares / count) - (avg * avg)

        return TradingSymbolStatsModel(
            min = stats.min,
            max = stats.max,
            last = stats.last,
            avg = avg,
            variance = variance,
        )
    }
}
