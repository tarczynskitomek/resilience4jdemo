plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.5.0")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:1.5.0")
    implementation("io.github.resilience4j:resilience4j-timelimiter:1.5.0")
    implementation("io.github.resilience4j:resilience4j-micrometer:1.5.0")
}
