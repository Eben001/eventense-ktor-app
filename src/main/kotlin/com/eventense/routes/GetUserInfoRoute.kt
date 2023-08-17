package com.eventense.routes

import com.eventense.domain.model.ApiResponse
import com.eventense.domain.model.EndPoint
import com.eventense.domain.model.UserSession
import com.eventense.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Routing.getUserInfoRoute(
    app:Application,
    userDataSource: UserDataSource
){
    authenticate("auth-session") {
        get(EndPoint.GetUserInfo.path){
            val userSession = call.principal<UserSession>() //retrieve session
            if(userSession == null){
                app.log.info("INVALID_SESSION")
                call.respondRedirect(EndPoint.UnAuthorized.path)
            }else{
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userId = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )

                }catch (e:Exception){
                    app.log.info("GETTING USER INFO ERROR ${e.message}")
                    call.respondRedirect(EndPoint.UnAuthorized.path)


                }
            }


        }
    }
}