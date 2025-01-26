package pl.dombur.trading.analyzer.infrastructure.storage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.dombur.trading.analyzer.domain.exception.SymbolNotFoundException

class TradingDataCacheTest {
    private val tradingDataCache = TradingDataCache()

    @Test
    fun `GIVEN some batches data WHEN add three times THEN should store new values successfully in the cache and get stats`() {
        // given
        val symbol = "BTC"
        val batch1 = (1..10).map { it.toDouble() }
        val batch2 = (11..20).map { it.toDouble() }
        val batch3 = (21..100).map { it.toDouble() }

        // when: add batches
        tradingDataCache.addBatch(symbol, batch1)
        tradingDataCache.addBatch(symbol, batch2)
        tradingDataCache.addBatch(symbol, batch3)

        // and: get stats for 10 data points
        val stats1 = tradingDataCache.getStats(symbol, 1)

        // then: should return stats of the latest data points
        assertThat(stats1.min).isEqualTo(91.0)
        assertThat(stats1.max).isEqualTo(100.0)
        assertThat(stats1.last).isEqualTo(100.0)
        assertThat(stats1.avg).isEqualTo(95.5)
        assertThat(stats1.variance).isEqualTo(8.25)

        // when: get stats for 100 data points
        val stats2 = tradingDataCache.getStats(symbol, 2)

        // then: should return stats of the latest data points
        assertThat(stats2.min).isEqualTo(1.0)
        assertThat(stats2.max).isEqualTo(100.0)
        assertThat(stats2.last).isEqualTo(100.0)
        assertThat(stats2.avg).isEqualTo(50.5)
        assertThat(stats2.variance).isEqualTo(833.25)
    }

    @Test
    fun `GIVEN no specified symbol in the cache WHEN get stats THEN should throw exception`() {
        // given
        val symbol = "BTC"
        val dataPointsExponent = 5

        // when
        val exception =
            assertThrows<SymbolNotFoundException> {
                tradingDataCache.getStats(symbol, dataPointsExponent)
            }

        // then
        assertThat(exception.message).isEqualTo("Symbol $symbol not found")
    }
}
