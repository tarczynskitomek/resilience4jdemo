package it.tarczynski.resilience4jdemo.foo.exception

import it.tarczynski.resilience4jdemo.foo.logging.loggerFor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SilenceOfTheLambs {

    private val logger = loggerFor<SilenceOfTheLambs>()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun printMessageOnly(e: Exception) {
        logger.error(e.message)
    }
}
