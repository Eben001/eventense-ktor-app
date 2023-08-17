package com.eventense.routes

import com.eventense.domain.model.ApiResponse
import com.eventense.domain.model.EndPoint
import com.eventense.domain.model.UserSession
import com.eventense.domain.model.UserUpdate
import com.eventense.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Routing.updateUserRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        put(EndPoint.UpdateUserInfo.path) {
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<UserUpdate>()

            if (userSession == null) {
                call.respondRedirect(EndPoint.UnAuthorized.path)
                app.log.info("INVALID SESSION")
            } else {
                try {
                    updateUserInfo(
                        app = app,
                        userId = userSession.id,
                        userUpdate = userUpdate,
                        userDataSource = userDataSource
                    )
                } catch (e: Exception) {
                    app.log.info("UPDATE USER INFO ERROR $e")
                    call.respondRedirect(EndPoint.UnAuthorized.path)
                }
            }
        }

    }


}

private suspend fun PipelineContext<Unit, ApplicationCall>.updateUserInfo(
    app: Application, userId: String,
    userUpdate: UserUpdate, userDataSource: UserDataSource
) {
    val response = userDataSource.updateUserInfo(
        userId = userId,
        firstName = userUpdate.firstName,
        lastName = userUpdate.lastName
    )
    if (response) {
        app.log.info("USER SUCCESSFULLY UPDATED")
        call.respond(
            message = ApiResponse(
                success = true,
                message = "Successfully updated"
            ),
            status = HttpStatusCode.OK

        )
    } else {
        app.log.info("ERROR WHILE UPDATING USER")
        call.respond(
            message = ApiResponse(success = false),
            status = HttpStatusCode.BadRequest
        )

    }
}