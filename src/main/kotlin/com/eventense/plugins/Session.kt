package com.eventense.plugins

import com.eventense.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.io.File
import kotlin.time.Duration.Companion.minutes

fun Application.configureSession() {
    install(Sessions) {
        val secretEncryptionKey = hex("0000112222233333aabbbbccccdddffff")
        val secretAuthKey = hex("989819289812491824912abcdd")

        cookie<UserSession>(
            name = "USER_SESSION",
            storage = directorySessionStorage(File(".sessions"))
        ){

            cookie.maxAge = 30.minutes
            transform(SessionTransportTransformerEncrypt(secretEncryptionKey, secretAuthKey))

        }
    }
}