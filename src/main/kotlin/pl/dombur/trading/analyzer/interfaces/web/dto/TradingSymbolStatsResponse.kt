package pl.dombur.trading.analyzer.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TradingSymbolStatsResponse(
    @Schema(
        description = "Minimum price in the last 1e^k data points.",
        example = "100.5",
    )
    val min: Double,
    @Schema(
        description = "Maximum price in the last 1e^k data points.",
        example = "200.75",
    )
    val max: Double,
    @Schema(
        description = "Most recent trading price.",
        example = "150.25",
    )
    val last: Double,
    @Schema(
        description = "Average price over the last 1e^k data points.",
        example = "145.67",
    )
    val avg: Double,
    @Schema(
        description = "Variance of prices over the last 1e^k data points.",
        example = "250.89",
    )
    val variance: Double,
)
