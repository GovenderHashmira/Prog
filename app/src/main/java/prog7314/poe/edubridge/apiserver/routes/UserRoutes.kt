package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import prog7314.poe.edubridge.apiserver.data.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// GET /api/users/me — return current authenticated user profile.

fun Route.userRoutes() {
    route("/api/users") {

        get("/me") {
            val token = call.request.headers["Authorization"]
            val userId = AuthTokenStore.userIdFor(token)

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid token"))
                return@get
            }

            val user = ApiDatabase.users.firstOrNull { it.id == userId }
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
                return@get
            }

            ApiLogger.d("GET /api/users/me userId=$userId role=${user.role}")
            call.respond(user.copy(password = ""))
        }
    }
}
