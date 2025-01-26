package pl.dombur.trading.analyzer.domain

import pl.dombur.trading.analyzer.interfaces.web.dto.AddBatchTradingDataRequest

data class AddBatchTradingDataCmd(
    val symbol: String,
    val values: List<Double>,
) {
    companion object {
        fun fromRequest(request: AddBatchTradingDataRequest) =
            AddBatchTradingDataCmd(
                symbol = request.symbol,
                values = request.values,
            )
    }
}
