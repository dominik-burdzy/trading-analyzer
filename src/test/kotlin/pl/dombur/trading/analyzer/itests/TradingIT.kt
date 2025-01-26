package pl.dombur.trading.analyzer.itests

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.dombur.trading.analyzer.config.BaseIT
import pl.dombur.trading.analyzer.domain.AddBatchTradingDataCmd
import pl.dombur.trading.analyzer.interfaces.web.dto.TradingSymbolStatsResponse
import kotlin.random.Random.Default.nextInt

class TradingIT : BaseIT() {
    @Test
    fun `GIVEN some requests with batch data WHEN add and then request data statistics THEN should return relevant data`() {
        // given: batches of trading data
        val batchETH1 =
            AddBatchTradingDataCmd(
                symbol = "ETHUSD",
                values = (1..10000).map { nextInt(3380, 3390).toDouble() },
            )
        val batchETH2 =
            AddBatchTradingDataCmd(
                symbol = "ETHUSD",
                values = (1..10000).map { nextInt(3390, 3400).toDouble() },
            )
        val batchBTC =
            AddBatchTradingDataCmd(
                symbol = "BTCUSD",
                values = (1..10000).map { nextInt(50000, 60000).toDouble() },
            )

        // when: add first batch of ETH data
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/tradings/add_batch")
                    .withBody(batchETH1),
            ).andExpect(MockMvcResultMatchers.status().isOk)

        // and: get statistics for ETH data
        val statsETH1 =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/ETHUSD?k=4"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then: should return relevant statistics
        assertThat(statsETH1.min).isEqualTo(batchETH1.values.min())
        assertThat(statsETH1.max).isEqualTo(batchETH1.values.max())
        assertThat(statsETH1.avg).isEqualTo(batchETH1.values.average())
        assertThat(statsETH1.last).isEqualTo(batchETH1.values.last())
        assertThat(statsETH1.variance).isEqualTo(batchETH1.values.variance())

        // when: add second batch of ETH data
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/tradings/add_batch")
                    .withBody(batchETH2),
            ).andExpect(MockMvcResultMatchers.status().isOk)

        // and: get statistics for ETH data
        val statsETH2 =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/ETHUSD?k=4"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then: should return relevant statistics
        assertThat(statsETH2.min).isEqualTo(batchETH2.values.min())
        assertThat(statsETH2.max).isEqualTo(batchETH2.values.max())
        assertThat(statsETH2.avg).isEqualTo(batchETH2.values.average())
        assertThat(statsETH2.last).isEqualTo(batchETH2.values.last())
        assertThat(statsETH2.variance).isEqualTo(batchETH2.values.variance())

        // when: add batch of BTC data
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/tradings/add_batch")
                    .withBody(batchBTC),
            ).andExpect(MockMvcResultMatchers.status().isOk)

        // and: get statistics for BTC data for 10^1 data points
        val statsBCT1 =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/BTCUSD?k=1"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then: should return relevant statistics
        assertThat(statsBCT1.min).isEqualTo(batchBTC.values.takeLast(10).min())
        assertThat(statsBCT1.max).isEqualTo(batchBTC.values.takeLast(10).max())
        assertThat(statsBCT1.avg).isEqualTo(batchBTC.values.takeLast(10).average())
        assertThat(statsBCT1.last).isEqualTo(batchBTC.values.last())
        assertThat(statsBCT1.variance).isEqualTo(batchBTC.values.takeLast(10).variance())

        // and: get statistics for BTC data for 10^2 data points
        val statsBCT2 =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/BTCUSD?k=2"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then: should return relevant statistics
        assertThat(statsBCT2.min).isEqualTo(batchBTC.values.takeLast(100).min())
        assertThat(statsBCT2.max).isEqualTo(batchBTC.values.takeLast(100).max())
        assertThat(statsBCT2.avg).isEqualTo(batchBTC.values.takeLast(100).average())
        assertThat(statsBCT2.last).isEqualTo(batchBTC.values.last())
        assertThat(statsBCT2.variance).isEqualTo(batchBTC.values.takeLast(100).variance())

        // and: get statistics for BTC data for 10^3 data points
        val statsBCT3 =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/BTCUSD?k=3"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then: should return relevant statistics
        assertThat(statsBCT3.min).isEqualTo(batchBTC.values.takeLast(1000).min())
        assertThat(statsBCT3.max).isEqualTo(batchBTC.values.takeLast(1000).max())
        assertThat(statsBCT3.avg).isEqualTo(batchBTC.values.takeLast(1000).average())
        assertThat(statsBCT3.last).isEqualTo(batchBTC.values.last())
        assertThat(statsBCT3.variance).isEqualTo(batchBTC.values.takeLast(1000).variance())

        // and: get statistics for BTC data for 10^4 data points
        val statsBCT4 =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/BTCUSD?k=4"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then: should return relevant statistics
        assertThat(statsBCT4.min).isEqualTo(batchBTC.values.min())
        assertThat(statsBCT4.max).isEqualTo(batchBTC.values.max())
        assertThat(statsBCT4.avg).isEqualTo(batchBTC.values.average())
        assertThat(statsBCT4.last).isEqualTo(batchBTC.values.last())
        assertThat(statsBCT4.variance).isEqualTo(batchBTC.values.variance())
    }

    private fun List<Double>.variance(): Double {
        val avg = average()
        val sumOfSquares = this.map { it * it }.sum() / size
        return sumOfSquares - avg * avg
    }
}
