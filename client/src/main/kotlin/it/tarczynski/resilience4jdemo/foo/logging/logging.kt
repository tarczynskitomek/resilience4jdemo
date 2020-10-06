package it.tarczynski.resilience4jdemo.foo.logging

import org.slf4j.LoggerFactory

inline fun <reified T> loggerFor() = LoggerFactory.getLogger(T::class.java)
