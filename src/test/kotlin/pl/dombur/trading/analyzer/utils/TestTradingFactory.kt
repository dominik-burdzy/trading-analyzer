package pl.dombur.trading.analyzer.utils

import pl.dombur.trading.analyzer.domain.AddBatchTradingDataCmd
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsCmd
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsModel

object TestTradingFactory {
    fun addBatchTradingDataCmd(
        symbol: String = "BTCUSD",
        values: List<Double> = listOf(1.0, 2.0, 3.0),
    ) = AddBatchTradingDataCmd(
        symbol = symbol,
        values = values,
    )

    fun tradingSymbolStatsCmd(
        symbol: String = "BTCUSD",
        dataPointsExponent: Int = 3,
    ) = TradingSymbolStatsCmd(
        symbol = symbol,
        dataPointsExponent = dataPointsExponent,
    )

    fun tradingSymbolStatsModel() =
        TradingSymbolStatsModel(
            min = 1.0,
            max = 3.0,
            last = 3.0,
            avg = 2.0,
            variance = 1.0,
        )
}
