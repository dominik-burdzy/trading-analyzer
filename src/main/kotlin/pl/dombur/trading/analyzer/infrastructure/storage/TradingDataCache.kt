package pl.dombur.trading.analyzer.infrastructure.storage

import org.springframework.stereotype.Component
import pl.dombur.trading.analyzer.domain.TradingDataStorage
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsModel
import pl.dombur.trading.analyzer.domain.exception.SymbolNotFoundException

@Component
class TradingDataCache : TradingDataStorage {
    private val cache = mutableMapOf<String, TradingSymbolData>()

    override fun addBatch(
        symbol: String,
        values: List<Double>,
    ) {
        cache.computeIfAbsent(symbol) { TradingSymbolData(symbol) }.addValues(values)
    }

    override fun getStats(
        symbol: String,
        dataPointsExponent: Int,
    ): TradingSymbolStatsModel =
        cache[symbol]?.getStats(dataPointsExponent)
            ?: throw SymbolNotFoundException(symbol)
}
