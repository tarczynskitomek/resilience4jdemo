package it.tarczynski.resilience4jdemo.foo.client

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import io.github.resilience4j.timelimiter.annotation.TimeLimiter
import it.tarczynski.resilience4jdemo.foo.logging.loggerFor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class DeclarativeFooServiceClient(restTemplateBuilder: RestTemplateBuilder) {

    private val logger = loggerFor<DeclarativeFooServiceClient>()

    private val restTemplate = restTemplateBuilder
            .rootUri("http://localhost:8081/api/v1/foos")
            .build()

    @Retry(name = "FOO")
    @Bulkhead(name = "FOO")
    @CircuitBreaker(name = "FOO")
    fun ok(): String = executeRequest("/ok")

    @Retry(name = "FOO")
    @Bulkhead(name = "FOO")
    @CircuitBreaker(name = "FOO")
    @TimeLimiter(name = "FOO", fallbackMethod = "slowFallback")
    fun slow(): String = executeRequest("/slow")

    @Retry(name = "FOO")
    @Bulkhead(name = "FOO")
    @CircuitBreaker(name = "FOO", fallbackMethod = "errorFallback")
    fun error(): String = executeRequest("/error")

    @Retry(name = "FOO")
    @Bulkhead(name = "FOO")
    @CircuitBreaker(name = "FOO")
    fun flaky(): String = executeRequest("/flaky")

    private fun executeRequest(uri: String): String {
        logger.warn("Trying... $uri")
        return restTemplate.getForObject(uri, String::class.java)
                ?: throw RuntimeException("Failed to get response body")
    }

    fun slowFallback(ignored: Exception) = "Gramma was slow, but she was old"

    fun errorFallback(ignored: Exception) = "Recovered, it's fine"
}
