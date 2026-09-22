package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import prog7314.poe.edubridge.apiserver.models.*
import prog7314.poe.edubridge.apiserver.data.*

/**
 * Authentication endpoints.
 *
 * POST /api/auth/sso      — SSO login (matches team Retrofit)
 * POST /api/auth/login    — legacy login (kept for direct testing)
 * POST /api/auth/register — create new parent account
 * POST /api/auth/refresh  — refresh access token
 * POST /api/auth/logout   — revoke token
 */
fun Route.authRoutes() {
    route("/api/auth") {

        // ⭐ Primary endpoint — matches EduBridgeApi.authenticateSso()
        post("/sso") {
            val body = call.receive<Map<String, String>>()
            val email = body["identityToken"]?.substringBefore(":") ?: body["email"] ?: ""
            val provider = body["provider"] ?: "mock"
            ApiLogger.d("POST /api/auth/sso provider=$provider email=$email")

            if (email.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing identity token"))
                return@post
            }

            // Find or create user by email
            var user = ApiDatabase.users.firstOrNull { it.email.equals(email, ignoreCase = true) }
            if (user == null) {
                user = ApiUser(
                    id = "u-${ApiDatabase.users.size + 1}",
                    name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email,
                    password = "sso-managed",
                    role = "Parent",
                    linkedStudentIds = listOf("stu-1")
                )
                ApiDatabase.users.add(user)
                ApiLogger.i("SSO auto-registered ${user.email}")
            }

            val accessToken = AuthTokenStore.issueToken(user.id, user.role)
            val refreshToken = AuthTokenStore.issueToken(user.id, user.role)

            call.respond(
                mapOf(
                    "accessToken" to accessToken,
                    "refreshToken" to refreshToken,
                    "expiresIn" to 3600,
                    "user" to mapOf(
                        "userId" to user.id,
                        "name" to user.name,
                        "email" to user.email,
                        "role" to user.role,
                        "language" to "en",
                        "biometricEnabled" to false,
                        "notificationEnabled" to true
                    )
                )
            )
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

            val token = AuthTokenStore.issueToken(user.id, user.role)
            val refreshToken = AuthTokenStore.issueToken(user.id, user.role)

            call.respond(
                mapOf(
                    "accessToken" to token,
                    "refreshToken" to refreshToken,
                    "expiresIn" to 3600,
                    "user" to mapOf(
                        "userId" to user.id,
                        "name" to user.name,
                        "email" to user.email,
                        "role" to user.role,
                        "language" to "en",
                        "biometricEnabled" to false,
                        "notificationEnabled" to true
                    )
                )
            )
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

            val newUser = ApiUser(
                id = "u-${ApiDatabase.users.size + 1}",
                name = req.email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = req.email,
                password = req.password,
                role = "Parent",
                linkedStudentIds = listOf("stu-1")
            )
            ApiDatabase.users.add(newUser)

            val token = AuthTokenStore.issueToken(newUser.id, newUser.role)
            val refreshToken = AuthTokenStore.issueToken(newUser.id, newUser.role)

            call.respond(
                mapOf(
                    "accessToken" to token,
                    "refreshToken" to refreshToken,
                    "expiresIn" to 3600,
                    "user" to mapOf(
                        "userId" to newUser.id,
                        "name" to newUser.name,
                        "email" to newUser.email,
                        "role" to newUser.role,
                        "language" to "en",
                        "biometricEnabled" to false,
                        "notificationEnabled" to true
                    )
                )
            )
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
                ?: return@post call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))

            val newToken = AuthTokenStore.issueToken(user.id, user.role)
            val newRefresh = AuthTokenStore.issueToken(user.id, user.role)

            call.respond(
                mapOf(
                    "accessToken" to newToken,
                    "refreshToken" to newRefresh,
                    "expiresIn" to 3600,
                    "user" to mapOf(
                        "userId" to user.id,
                        "name" to user.name,
                        "email" to user.email,
                        "role" to user.role,
                        "language" to "en",
                        "biometricEnabled" to false,
                        "notificationEnabled" to true
                    )
                )
            )
        }

        // Logout
        post("/logout") {
            val token = call.request.headers["Authorization"]
            if (token != null) AuthTokenStore.revoke(token)
            call.respond(mapOf("message" to "Logged out"))
        }
    }
}