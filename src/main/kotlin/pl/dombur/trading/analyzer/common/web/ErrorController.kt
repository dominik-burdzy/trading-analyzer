package pl.dombur.trading.analyzer.common.web

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pl.dombur.trading.analyzer.domain.exception.NotEnoughTradingDataException
import pl.dombur.trading.analyzer.domain.exception.QueryOutOfRangeException
import pl.dombur.trading.analyzer.domain.exception.SymbolNotFoundException

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class ErrorController {
    @ExceptionHandler(SymbolNotFoundException::class)
    fun handleNotFound(e: SymbolNotFoundException): ResponseEntity<Any> {
        logger.warn(e) { "Cannot find symbol" }
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(QueryOutOfRangeException::class)
    fun handleQueryOutOfRange(e: QueryOutOfRangeException): ResponseEntity<Any> {
        logger.warn(e) { "Query out of range" }
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotEnoughTradingDataException::class)
    fun handleNotEnoughTradingData(e: NotEnoughTradingDataException): ResponseEntity<Any> {
        logger.warn(e) { "Not enough trading data" }
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }
}
