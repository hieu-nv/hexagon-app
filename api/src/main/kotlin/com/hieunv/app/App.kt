package com.hieunv.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.hieunv.app", "com.hieunv.gw"])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
