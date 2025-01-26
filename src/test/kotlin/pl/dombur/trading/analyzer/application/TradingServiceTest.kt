package pl.dombur.trading.analyzer.application

import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import pl.dombur.trading.analyzer.domain.TradingDataStorage
import pl.dombur.trading.analyzer.utils.TestTradingFactory

class TradingServiceTest {
    private val tradingDataStorage = mock<TradingDataStorage>()

    private val tradingService = TradingService(tradingDataStorage)

    @Test
    fun `GIVEN cmd with batch data WHEN add THEN should store new values successfully in the storage`() {
        // given
        val cmd = TestTradingFactory.addBatchTradingDataCmd()

        // when
        tradingService.addBatch(cmd)

        // then
        verify(tradingDataStorage, times(1)).addBatch(eq(cmd.symbol), eq(cmd.values))
    }

    @Test
    fun `GIVEN some trading symbol data WHEN get stats THEN should return them successfully`() {
        // given
        val cmd = TestTradingFactory.tradingSymbolStatsCmd()

        // when
        tradingService.getStats(cmd)

        // then
        verify(tradingDataStorage, times(1)).getStats(eq(cmd.symbol), eq(cmd.dataPointsExponent))
    }
}
