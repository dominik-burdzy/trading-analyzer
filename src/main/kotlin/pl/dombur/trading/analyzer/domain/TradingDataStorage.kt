package pl.dombur.trading.analyzer.domain

interface TradingDataStorage {
    fun addBatch(
        symbol: String,
        values: List<Double>,
    )

    fun getStats(
        symbol: String,
        dataPointsExponent: Int,
    ): TradingSymbolStatsModel
}
