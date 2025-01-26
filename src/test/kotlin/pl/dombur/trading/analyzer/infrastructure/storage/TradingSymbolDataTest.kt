package pl.dombur.trading.analyzer.infrastructure.storage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.dombur.trading.analyzer.domain.exception.NotEnoughTradingDataException
import pl.dombur.trading.analyzer.domain.exception.QueryOutOfRangeException

class TradingSymbolDataTest {
    private val tradingSymbolData = TradingSymbolData("ETHUSD")

    @Test
    fun `GIVEN new values WHEN add THEN should store them successfully to every window`() {
        // given
        val values = (1..10_000).map { it.toDouble() }

        // when: add values
        tradingSymbolData.addValues(values)

        // and: get stats for 10^1 data points
        val stats1 = tradingSymbolData.getStats(1)

        // then: should return stats of the latest data points
        assertThat(stats1.min).isEqualTo(9_991.0)
        assertThat(stats1.max).isEqualTo(10_000.0)
        assertThat(stats1.last).isEqualTo(10_000.0)
        assertThat(stats1.avg).isEqualTo(9_995.5)
        assertThat(stats1.variance).isEqualTo(8.25)

        // when: get stats for 10^2 data points
        val stats2 = tradingSymbolData.getStats(2)

        // then: should return stats of the latest data points
        assertThat(stats2.min).isEqualTo(9_901.0)
        assertThat(stats2.max).isEqualTo(10_000.0)
        assertThat(stats2.last).isEqualTo(10_000.0)
        assertThat(stats2.avg).isEqualTo(9_950.5)
        assertThat(stats2.variance).isEqualTo(833.25)

        // when: get stats for 10^3 data points
        val stats3 = tradingSymbolData.getStats(3)

        // then: should return stats of the latest data points
        assertThat(stats3.min).isEqualTo(9_001.0)
        assertThat(stats3.max).isEqualTo(10_000.0)
        assertThat(stats3.last).isEqualTo(10_000.0)
        assertThat(stats3.avg).isEqualTo(9_500.5)
        assertThat(stats3.variance).isEqualTo(83_333.25)

        // when: get stats for 10^4 data points
        val stats4 = tradingSymbolData.getStats(4)

        // then: should return stats of the latest data points
        assertThat(stats4.min).isEqualTo(1.0)
        assertThat(stats4.max).isEqualTo(10_000.0)
        assertThat(stats4.last).isEqualTo(10_000.0)
        assertThat(stats4.avg).isEqualTo(5_000.5)
        assertThat(stats4.variance).isEqualTo(8_333_333.25)

        // when: get stats for 10^5 data points / then: should throw NotEnoughTradingDataException exception
        assertThrows<NotEnoughTradingDataException> {
            tradingSymbolData.getStats(5)
        }

        // when: get stats for 10^6 data points / then: should throw NotEnoughTradingDataException exception
        assertThrows<NotEnoughTradingDataException> {
            tradingSymbolData.getStats(6)
        }

        // when: get stats for 10^7 data points / then: should throw NotEnoughTradingDataException exception
        assertThrows<NotEnoughTradingDataException> {
            tradingSymbolData.getStats(7)
        }

        // when: get stats for 10^8 data points / then: should throw NotEnoughTradingDataException exception
        assertThrows<NotEnoughTradingDataException> {
            tradingSymbolData.getStats(8)
        }

        // when: get stats for 10^9 data points / then: should throw QueryOutOfRangeException exception
        assertThrows<QueryOutOfRangeException> {
            tradingSymbolData.getStats(9)
        }
    }
}
