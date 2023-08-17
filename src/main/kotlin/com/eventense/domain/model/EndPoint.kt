package com.eventense.domain.model

sealed class EndPoint(val path: String) {
    object Root : EndPoint(path = "/")
    object TokenVerification : EndPoint(path = "/token_verification")
    object GetUserInfo : EndPoint(path = "/get_user")
    object UpdateUserInfo : EndPoint(path = "/update_user")
    object DeleteUser : EndPoint(path = "/delete_user")
    object GetEvents: EndPoint(path = "/get_events")
    object CreateEvent:EndPoint(path = "/create_event")
    object DeleteEvent:EndPoint(path = "/delete_event")
    object AttendEvent:EndPoint(path = "/attend_event")
    object SignOut : EndPoint(path = "/sign_out")
    object UnAuthorized : EndPoint(path = "/unauthorized")
    object Authorized : EndPoint(path = "/authorized")

}