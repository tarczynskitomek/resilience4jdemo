package it.tarczynski.resilience4jdemo

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import it.tarczynski.resilience4jdemo.foo.logging.loggerFor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class Resilience4jDemoApplication

fun main(args: Array<String>) {
    runApplication<Resilience4jDemoApplication>(*args)
}

@Configuration
class EventLoggingConfig(circuitBreakerRegistry: CircuitBreakerRegistry) {

    private val logger = loggerFor<EventLoggingConfig>()

    init {
        circuitBreakerRegistry.circuitBreaker("FOO")
                .eventPublisher
                .onStateTransition {
                    logger.warn("Circuit breaker state transition to ${it.stateTransition}")
                }
    }
}
