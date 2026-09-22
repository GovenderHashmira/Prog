package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import prog7314.poe.edubridge.apiserver.models.*
import prog7314.poe.edubridge.apiserver.data.*
import java.time.Instant

/**
 * Authentication endpoints.
 *
 * POST /api/auth/sso      — login used by the app (matches EduBridgeApi.authenticateSso())
 * POST /api/auth/login    — legacy login (kept for direct testing)
 * POST /api/auth/register — create new parent account
 * POST /api/auth/refresh  — refresh access token
 * POST /api/auth/logout   — revoke token
 */
fun Route.authRoutes() {
    route("/api/auth") {

        // Body: { provider, identityToken, deviceId }
        // Mock SSO: identityToken is "email:password".
        // provider == "register" creates the account if it doesn't exist yet.
        post("/sso") {
            val body = call.receive<Map<String, String>>()
            val token = body["identityToken"].orEmpty()
            val email = token.substringBefore(":").trim()
            val password = if (":" in token) token.substringAfter(":") else ""
            val provider = body["provider"] ?: "local"
            ApiLogger.d("POST /api/auth/sso provider=$provider email=$email")

            if (email.isBlank() || password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Email and password required"))
                return@post
            }

            var user = ApiDatabase.users.firstOrNull { it.email.equals(email, ignoreCase = true) }

            if (provider == "register") {
                if (user != null) {
                    call.respond(HttpStatusCode.Conflict, mapOf("error" to "Email already registered"))
                    return@post
                }
                user = newParent(email, password)
                ApiDatabase.users.add(user)
                ApiLogger.i("Registered ${user.email}")
            } else {
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No account found for this email"))
                    return@post
                }
                if (user.password != password) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                    return@post
                }
            }

            call.respond(authResponse(user))
        }

        // Legacy login (still works for testing with credentials)
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
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                return@post
            }

            call.respond(authResponse(user))
        }

        // Register
        post("/register") {
            val req = call.receive<ApiLoginRequest>()
            ApiLogger.d("POST /api/auth/register email=${req.email}")

            if (req.email.isBlank() || req.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Email and password required"))
                return@post
            }
            if (req.password.length < 6) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Password must be at least 6 characters"))
                return@post
            }

            val exists = ApiDatabase.users.any { it.email.equals(req.email, ignoreCase = true) }
            if (exists) {
                call.respond(HttpStatusCode.Conflict, mapOf("error" to "Email already registered"))
                return@post
            }

            val newUser = newParent(req.email, req.password)
            ApiDatabase.users.add(newUser)
            call.respond(authResponse(newUser))
        }

        // Refresh
        post("/refresh") {
            val body = call.receive<Map<String, String>>()
            val refreshToken = body["refreshToken"] ?: ""
            val userId = AuthTokenStore.userIdFor(refreshToken)

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid refresh token"))
                return@post
            }

            val user = ApiDatabase.users.firstOrNull { it.id == userId }
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
                return@post
            }

            call.respond(authResponse(user))
        }

        // Logout
        post("/logout") {
            val token = call.request.headers["Authorization"]
            if (token != null) AuthTokenStore.revoke(token)
            call.respond(mapOf("message" to "Logged out"))
        }
    }
}

private fun newParent(email: String, password: String) = ApiUser(
    id = "u-${ApiDatabase.users.size + 1}",
    name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
    email = email,
    password = password,
    role = "Parent",
    linkedStudentIds = listOf("stu-1")
)

/**
 * Shape must match AuthResponse / UserProfileDto on the Retrofit side.
 * createdAt/updatedAt are non-null in UserProfileDto, so they must be sent.
 */
private fun authResponse(user: ApiUser): Map<String, Any> {
    val now = Instant.now().toString()
    return mapOf(
        "accessToken" to AuthTokenStore.issueToken(user.id, user.role),
        "refreshToken" to AuthTokenStore.issueToken(user.id, user.role),
        "expiresIn" to 3600,
        "user" to mapOf(
            "userId" to user.id,
            "name" to user.name,
            "email" to user.email,
            "role" to user.role,
            "language" to "en",
            "createdAt" to now,
            "updatedAt" to now
        )
    )
}
