package com.eventense

import com.eventense.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureAuth()
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureSession()

}
