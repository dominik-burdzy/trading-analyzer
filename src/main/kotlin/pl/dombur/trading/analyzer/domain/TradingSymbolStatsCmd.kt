package pl.dombur.trading.analyzer.domain

data class TradingSymbolStatsCmd(
    val symbol: String,
    val dataPointsExponent: Int,
)
