package it.tarczynski.resilience4jdemo.foo.enpoint

import it.tarczynski.resilience4jdemo.foo.client.DeclarativeFooServiceClient
import it.tarczynski.resilience4jdemo.foo.client.ProgrammaticFooServiceClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/foos")
class FooEndpoint(private val declarativeClient: DeclarativeFooServiceClient,
                  private val programmaticClient: ProgrammaticFooServiceClient) {

    @GetMapping("ok")
    fun okFoo(): String = declarativeClient.ok()

    @GetMapping("slow")
    fun slowFoo(): String = declarativeClient.slow()

    @GetMapping("error")
    fun error(): String = declarativeClient.error()
    
    @GetMapping("flaky")
    fun random(): String = declarativeClient.flaky()


    @GetMapping("programmatic/ok")
    fun programmaticOkFoo(): String = programmaticClient.ok()

    @GetMapping("programmatic/slow")
    fun programmaticSlowFoo(): String = programmaticClient.slow()

    @GetMapping("programmatic/error")
    fun programmaticError(): String = programmaticClient.error()

    @GetMapping("programmatic/flaky")
    fun programmaticRandom(): String = programmaticClient.flaky()
}
