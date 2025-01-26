package pl.dombur.trading.analyzer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIT {
    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    protected lateinit var mapper: ObjectMapper

    internal fun <T> MockHttpServletRequestBuilder.withBody(body: T): MockHttpServletRequestBuilder =
        this.content(mapper.writeValueAsString(body)).contentType(MediaType.APPLICATION_JSON)

    internal final inline fun <reified T> ResultActions.andParsedResponse(): T = andReturn().response.contentAsString.let { mapper.readValue(it) }
}
