package it.tarczynski.resilience4jdemo.foo.enpoint

import it.tarczynski.resilience4jdemo.foo.service.FooServiceClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/foos")
class FooEndpoint(private val fooServiceClient: FooServiceClient) {

    @GetMapping("ok")
    fun okFoo(): String = fooServiceClient.ok()

    @GetMapping("slow")
    fun slowFoo(): String = fooServiceClient.slowFoo()

    @GetMapping("error")
    fun error(): String = fooServiceClient.error()

    @GetMapping("flaky")
    fun random(): String = fooServiceClient.random()
}
