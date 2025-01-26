package pl.dombur.trading.analyzer.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

data class AddBatchTradingDataRequest(
    @Schema(description = "String identifier for the financial instrument", example = "AAPL")
    val symbol: String,
    @field:NotEmpty
    @Schema(
        description = "Array of up to 10000 floating-point numbers representing sequential trading prices ordered from oldest to newest",
        example = "[100.0, 101.0, 102.0]",
    )
    val values: List<Double>,
)
