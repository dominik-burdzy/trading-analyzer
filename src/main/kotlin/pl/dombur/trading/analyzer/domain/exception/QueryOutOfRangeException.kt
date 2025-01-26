package pl.dombur.trading.analyzer.domain.exception

class QueryOutOfRangeException(
    dataPointsExponent: Int,
) : RuntimeException(
        "Query for data points exponent: $dataPointsExponent is out of range. It must be between 1 and 8.",
    )
