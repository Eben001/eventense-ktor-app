package com.eventense.routes

import com.eventense.domain.model.ApiResponse
import com.eventense.domain.model.EndPoint
import com.eventense.domain.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Routing.signOutUser(app: Application){

    authenticate("auth-session") {
        get(EndPoint.SignOut.path) {
            val userSession = call.principal<UserSession>()

            if(userSession == null){
                call.respondRedirect(EndPoint.UnAuthorized.path)
                app.log.info("INVALID SESSION")
            }else{
                try {
                    call.sessions.clear<UserSession>()
                    call.respond(message = ApiResponse(
                        success = true,
                        message = "Successfully signed out"
                    ), status = HttpStatusCode.OK)

                } catch (e: Exception) {
                    app.log.info("SIGN OUT ERROR $e")
                    call.respond(
                        message = ApiResponse(success = false),
                        status = HttpStatusCode.BadRequest
                    )

                }
            }
        }
    }

}