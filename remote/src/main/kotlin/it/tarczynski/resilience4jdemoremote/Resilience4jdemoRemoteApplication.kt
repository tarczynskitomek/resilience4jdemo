package it.tarczynski.resilience4jdemoremote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom

@SpringBootApplication
class Resilience4jdemoRemoteApplication

fun main(args: Array<String>) {
    runApplication<Resilience4jdemoRemoteApplication>(*args)
}

@RestController
@RequestMapping("/api/v1/foos")
class FooEndpoint {

    @GetMapping("ok")
    fun okFoo(): String {
//        Bulkhead: Thread.sleep(100L)
        return "OK"
    }

    @GetMapping("slow")
    fun slowFoo(): String {
        Thread.sleep(Duration.ofSeconds(3).toMillis())
        return "I'm a bit slow"
    }

    @GetMapping("error")
    fun error(): String {
        throw IOException("This is crazy, I'm out!")
    }

    @GetMapping("flaky")
    fun random(): String {
        val dice = ThreadLocalRandom.current().nextInt(0, 10)
        return if (dice == 9) throw IOException("Oh, bad luck")
        else "Fine, this time you were lucky"
    }

}
