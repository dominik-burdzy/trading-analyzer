package pl.dombur.trading.analyzer.domain.exception

class NotEnoughTradingDataException(
    symbol: String,
) : RuntimeException("Not enough trading data for symbol: $symbol.")
