package it.tarczynski.resilience4jdemo.foo.client

import io.github.resilience4j.bulkhead.Bulkhead
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.decorators.Decorators
import io.github.resilience4j.retry.Retry
import io.vavr.control.Try
import it.tarczynski.resilience4jdemo.foo.logging.loggerFor
import org.slf4j.Logger
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class ProgrammaticFooServiceClient(restTemplateBuilder: RestTemplateBuilder) {

    private val logger: Logger = loggerFor<ProgrammaticFooServiceClient>()

    private val restTemplate = restTemplateBuilder
            .rootUri("http://localhost:8081/api/v1/foos")
            .build()

    private val circuitBreaker: CircuitBreaker = CircuitBreaker.ofDefaults("BAR")
    private val bulkhead: Bulkhead = Bulkhead.ofDefaults("BAR")
    private val retry: Retry = Retry.ofDefaults("BAR")

    fun ok(): String = circuitBreaker.executeSupplier {
        executeRequest("/ok")
    }

    fun slow(): String = circuitBreaker.executeSupplier {
        executeRequest("/slow")
    }

    fun error(): String {

        val decorated = Decorators.ofSupplier { executeRequest("/error") }
                .withBulkhead(bulkhead)
                .withRetry(retry)
                .withCircuitBreaker(circuitBreaker)
                .decorate()

        return Try.ofSupplier(decorated).recover { "Recovered, It's fine" }.get()
    }

    // no retries here!
    fun flaky(): String {
        return Decorators.ofSupplier { executeRequest("/flaky") }
                .withBulkhead(bulkhead)
                .withCircuitBreaker(circuitBreaker)
                .withFallback { _ -> "It's flaky, but I've recovered!" }
                .decorate()
                .get()
    }

    private fun executeRequest(uri: String): String {
        logger.warn("Trying... $uri")
        return restTemplate.getForObject(uri, String::class.java)
                ?: throw RuntimeException("Failed to get response body")
    }
}
