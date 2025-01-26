package pl.dombur.trading.analyzer.interfaces.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.dombur.trading.analyzer.application.TradingService
import pl.dombur.trading.analyzer.domain.AddBatchTradingDataCmd
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsCmd
import pl.dombur.trading.analyzer.interfaces.web.dto.AddBatchTradingDataRequest
import pl.dombur.trading.analyzer.interfaces.web.dto.TradingSymbolStatsResponse

@RestController
@RequestMapping(TradingController.PATH)
class TradingController(
    private val tradingService: TradingService,
) {
    companion object {
        const val PATH = "/api/v1/public/tradings"
    }

    @Operation(
        summary = "Allows the bulk addition of consecutive trading data points for specific symbol",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "400",
                description = "On invalid request",
            ),
        ],
    )
    @PostMapping("/add_batch")
    fun addBatch(
        @RequestBody @Valid request: AddBatchTradingDataRequest,
    ) {
        val cmd = AddBatchTradingDataCmd.fromRequest(request)
        return tradingService.addBatch(cmd)
    }

    @Operation(
        summary = "Provides rapid statistical analyses of recent trading data for specified symbols",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "400",
                description = "On invalid request",
            ),
            ApiResponse(
                responseCode = "404",
                description = "On symbol not found",
            ),
        ],
    )
    @GetMapping("/stats/{symbol}")
    fun getStats(
        @PathVariable
        symbol: String,
        @Min(1)
        @Max(8)
        @RequestParam
        @Parameter(description = "An integer from 1 to 8, specifying the number of last 1e^{k} data points to analyze")
        k: Int,
    ): TradingSymbolStatsResponse {
        val cmd = TradingSymbolStatsCmd(symbol = symbol, dataPointsExponent = k)
        return tradingService.getStats(cmd).toResponse()
    }
}
