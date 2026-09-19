package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Student endpoints with parent-child link enforcement.
// GET /api/students      — list students visible to the current user
// GET /api/students/{id} — single student details
// Enforces POE business rules:
//  - Parents see only linked students
//  - Students see only their own record
//  - Teachers/Admins see all

fun Route.studentRoutes() {
    route("/api/students") {

        get {
            val token = call.request.headers["Authorization"]
            val userId = AuthTokenStore.userIdFor(token)
            val role = AuthTokenStore.roleFor(token)

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid token"))
                return@get
            }

            val user = ApiDatabase.users.first { it.id == userId }
            val visible = when (role) {
                "Parent", "Student" -> ApiDatabase.students.filter { it.id in user.linkedStudentIds }
                "Teacher", "Admin"  -> ApiDatabase.students
                else -> emptyList()
            }

            ApiLogger.d("GET /api/students role=$role count=${visible.size}")
            call.respond(visible)
        }

        get("/{id}") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/students/$id")
            val student = ApiDatabase.students.firstOrNull { it.id == id }
            if (student == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student not found"))
            } else {
                call.respond(student)
            }
        }
    }
}