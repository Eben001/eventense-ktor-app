package com.eventense.routes

import com.eventense.domain.model.ApiRequest
import com.eventense.domain.model.EndPoint
import com.eventense.domain.model.User
import com.eventense.domain.model.UserSession
import com.eventense.domain.repository.UserDataSource
import com.eventense.util.Constants.AUDIENCE
import com.eventense.util.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Routing.tokenVerificationRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    post(EndPoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)
            if (result != null) {
                saveUserToDatabase(
                    app = app,
                    result = result,
                    userDataSource = userDataSource
                )
            } else {
                app.log.info("TOKEN VERIFICATION FAILED")
                call.respondRedirect(EndPoint.UnAuthorized.path)
            }
        } else {
            app.log.info("EMPTY TOKEN ID")
            call.respondRedirect(EndPoint.UnAuthorized.path)
        }

    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(
    app: Application,
    result: GoogleIdToken,
    userDataSource: UserDataSource
) {
    val sub = result.payload["sub"].toString()
    val name = result.payload["name"].toString()
    val emailAddress = result.payload["email"].toString()
    val profileImage = result.payload["picture"].toString()
    val user = User(
        id = sub, name = name,
        emailAddress = emailAddress,
        profileImage = profileImage
    )
    val response = userDataSource.saveUserInfo(user = user)
    if (response) {
        app.log.info("USER SUCCESSFULLY SAVED/RETRIEVED")
        call.sessions.set(UserSession(id = sub, name = name))
        call.respondRedirect(EndPoint.Authorized.path)
    } else {
        app.log.info("ERROR SAVING USER")

        call.respondRedirect(EndPoint.UnAuthorized.path)
    }


}

fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? {
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(
            NetHttpTransport(), GsonFactory()
        ).setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()

        verifier.verify(tokenId)
    } catch (e: Exception) {

        null

    }


}