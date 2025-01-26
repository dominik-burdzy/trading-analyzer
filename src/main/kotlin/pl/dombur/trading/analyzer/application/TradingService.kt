package pl.dombur.trading.analyzer.application

import mu.KotlinLogging
import org.springframework.stereotype.Service
import pl.dombur.trading.analyzer.domain.AddBatchTradingDataCmd
import pl.dombur.trading.analyzer.domain.TradingDataStorage
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsCmd
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsModel

private val logger = KotlinLogging.logger {}

@Service
class TradingService(
    private val tradingDataStorage: TradingDataStorage,
) {
    fun addBatch(cmd: AddBatchTradingDataCmd) {
        logger.info { "Adding ${cmd.values.size} data points of ${cmd.symbol}" }
        tradingDataStorage.addBatch(cmd.symbol, cmd.values)
    }

    fun getStats(cmd: TradingSymbolStatsCmd): TradingSymbolStatsModel {
        logger.info { "Getting statistics of ${cmd.symbol} for the latest 1e^${cmd.dataPointsExponent} data points" }
        return tradingDataStorage.getStats(cmd.symbol, cmd.dataPointsExponent)
    }
}
