package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Authentication endpoints
// POST / api/auth/login - authenticate and receive token
// POST /api/auth/logout — revoke current token

fun Route.authRoutes() {
    route("/api/auth") {

        post("/login") {
            val req = call.receive<ApiLoginRequest>()
            ApiLogger.d("POST /api/auth/login email=${req.email}")

            if (req.email.isBlank() || req.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing credentials"))
                return@post
            }

            val user = ApiDatabase.users.firstOrNull {
                it.email.equals(req.email, ignoreCase = true) && it.password == req.password
            }

            if (user == null) {
                ApiLogger.w("Login failed for ${req.email}")
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                return@post
            }

            val token = AuthTokenStore.issueToken(user.id, user.role)
            ApiLogger.i("Login success userId=${user.id} role=${user.role}")

            call.respond(
                ApiLoginResponse(
                    token = token,
                    role = user.role,
                    userId = user.id,
                    userName = user.name,
                    email = user.email
                )
            )
        }

        post("/logout") {
            val token = call.request.headers["Authorization"]
            if (token != null) AuthTokenStore.revoke(token)
            call.respond(mapOf("message" to "Logged out"))
        }
    }
}