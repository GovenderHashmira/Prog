package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import prog7314.poe.edubridge.apiserver.models.ApiUserSettings
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant

// User settings endpoints.
// GET /api/users/me/settings
// PUT /api/users/me/settings

fun Route.settingsRoutes() {
    route("/api/users/me/settings") {

        get {
            val token = call.request.headers["Authorization"]
            val userId = AuthTokenStore.userIdFor(token)
                ?: return@get call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid token"))

            val settings = ApiDatabase.settings[userId] ?: ApiUserSettings(userId)
            ApiLogger.d("GET /api/users/me/settings userId=$userId")
            call.respond(settings)
        }

        put {
            val incoming = call.receive<ApiUserSettings>()
            val updated = incoming.copy(updatedAt = Instant.now().toString())
            ApiDatabase.settings[incoming.userId] = updated
            ApiLogger.i(
                "PUT /api/users/me/settings userId=${updated.userId} " +
                        "lang=${updated.language} notif=${updated.notificationsEnabled}"
            )
            call.respond(updated)
        }
    }
}