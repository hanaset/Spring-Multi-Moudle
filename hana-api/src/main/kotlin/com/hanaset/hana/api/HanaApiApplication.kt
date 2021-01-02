package com.hanaset.hana.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HanaApiApplication

fun main(args: Array<String>) {
	runApplication<HanaApiApplication>(*args)
}
