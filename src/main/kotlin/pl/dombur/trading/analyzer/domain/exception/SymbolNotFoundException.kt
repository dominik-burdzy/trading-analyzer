package pl.dombur.trading.analyzer.domain.exception

class SymbolNotFoundException(
    symbol: String,
) : RuntimeException("Symbol $symbol not found")
