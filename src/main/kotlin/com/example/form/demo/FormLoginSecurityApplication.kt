package com.example.form.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FormLoginSecurityApplication

fun main(args: Array<String>) {
    val application = SpringApplication(FormLoginSecurityApplication::class.java)
    application.webApplicationType = WebApplicationType.REACTIVE
    runApplication<FormLoginSecurityApplication>(*args)
}
