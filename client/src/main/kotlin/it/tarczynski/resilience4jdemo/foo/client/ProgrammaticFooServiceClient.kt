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
                .withCircuitBreaker(circuitBreaker)
                .withRetry(retry)
                .withBulkhead(bulkhead)
                .decorate()

        return Try.ofSupplier(decorated).recover { "Recovered, It's fine" }.get()
    }

    fun flaky(): String {

        // no retries here!
        val decorated = Decorators.ofSupplier { executeRequest("/flaky") }
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .decorate()

        return Try.ofSupplier(decorated).recover { "It's flaky, but I've recovered!" }.get()
    }

    private fun executeRequest(uri: String): String {
        logger.warn("Trying... $uri")
        return restTemplate.getForObject(uri, String::class.java)
                ?: throw RuntimeException("Failed to get response body")
    }
}
