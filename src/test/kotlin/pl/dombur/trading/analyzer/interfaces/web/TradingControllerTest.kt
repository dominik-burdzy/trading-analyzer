package pl.dombur.trading.analyzer.interfaces.web

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.dombur.trading.analyzer.application.TradingService
import pl.dombur.trading.analyzer.config.BaseWebMvcTest
import pl.dombur.trading.analyzer.domain.AddBatchTradingDataCmd
import pl.dombur.trading.analyzer.domain.TradingSymbolStatsCmd
import pl.dombur.trading.analyzer.domain.exception.NotEnoughTradingDataException
import pl.dombur.trading.analyzer.domain.exception.QueryOutOfRangeException
import pl.dombur.trading.analyzer.domain.exception.SymbolNotFoundException
import pl.dombur.trading.analyzer.interfaces.web.dto.TradingSymbolStatsResponse
import pl.dombur.trading.analyzer.utils.TestTradingFactory

@ContextConfiguration(classes = [TradingController::class])
class TradingControllerTest : BaseWebMvcTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var tradingService: TradingService

    @Test
    fun `GIVEN request with batch data WHEN add THEN should new values successfully`() {
        // given
        val request = TestTradingFactory.addBatchTradingDataCmd()

        // when
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/tradings/add_batch")
                    .withBody(request),
            ).andExpect(MockMvcResultMatchers.status().isOk)

        // then
        verify(tradingService, times(1)).addBatch(
            eq(
                AddBatchTradingDataCmd(
                    symbol = request.symbol,
                    values = request.values,
                ),
            ),
        )
    }

    @Test
    fun `GIVEN request with empty batch data WHEN add THEN should return 400`() {
        // given
        val request = TestTradingFactory.addBatchTradingDataCmd(values = emptyList())

        // when
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/tradings/add_batch")
                    .withBody(request),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)

        // then
        verify(tradingService, never()).addBatch(any())
    }

    @Test
    fun `GIVEN some trading data WHEN get symbol stats THEN should return it successfully`() {
        // given
        val symbol = "BTCUSD"
        val k = 1

        val model = TestTradingFactory.tradingSymbolStatsModel()

        given(tradingService.getStats(eq(TradingSymbolStatsCmd(symbol, k)))).willReturn(model)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/tradings/stats/$symbol?k=$k"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<TradingSymbolStatsResponse>()

        // then
        assertThat(result.min).isEqualTo(model.min)
        assertThat(result.max).isEqualTo(model.max)
        assertThat(result.last).isEqualTo(model.last)
        assertThat(result.avg).isEqualTo(model.avg)
        assertThat(result.variance).isEqualTo(model.variance)
    }

    @Test
    fun `GIVEN no trading symbol data WHEN get stats THEN should return 404`() {
        // given
        val symbol = "BTCUSD"
        val k = 3

        given(tradingService.getStats(eq(TradingSymbolStatsCmd(symbol, k)))).willThrow(SymbolNotFoundException(symbol))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/public/tradings/stats/$symbol?k=$k"),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `GIVEN k param out of range WHEN get symbol stats THEN should return 400`() {
        // given
        val symbol = "BTCUSD"
        val k = 9

        given(tradingService.getStats(eq(TradingSymbolStatsCmd(symbol, k)))).willThrow(QueryOutOfRangeException(k))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/public/tradings/stats/$symbol?k=$k"),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `GIVEN not enough data of given symbol WHEN get symbol stats THEN should return 400`() {
        // given
        val symbol = "BTCUSD"
        val k = 8

        given(
            tradingService.getStats(eq(TradingSymbolStatsCmd(symbol, k))),
        ).willThrow(NotEnoughTradingDataException(symbol))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/public/tradings/stats/$symbol?k=$k"),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
