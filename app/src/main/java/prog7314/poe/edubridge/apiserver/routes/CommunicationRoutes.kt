package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import prog7314.poe.edubridge.apiserver.data.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Notices and messages endpoints.
// GET /api/notices
// GET /api/messages
// GET /api/messages/{id}

fun Route.communicationRoutes() {

    route("/api/notices") {
        get {
            ApiLogger.d("GET /api/notices")
            call.respond(ApiDatabase.notices)
        }
    }

    route("/api/messages") {
        get {
            ApiLogger.d("GET /api/messages")
            call.respond(ApiDatabase.messages)
        }
        get("/{id}") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/messages/$id")
            val msg = ApiDatabase.messages.firstOrNull { it.id == id }
            if (msg == null) call.respond(HttpStatusCode.NotFound, mapOf("error" to "Not found"))
            else call.respond(msg)
        }
    }
}