package pl.dombur.trading.analyzer.domain

import pl.dombur.trading.analyzer.interfaces.web.dto.TradingSymbolStatsResponse

data class TradingSymbolStatsModel(
    val min: Double,
    val max: Double,
    val last: Double,
    val avg: Double,
    val variance: Double,
) {
    fun toResponse() =
        TradingSymbolStatsResponse(
            min = min,
            max = max,
            last = last,
            avg = avg,
            variance = variance,
        )
}
