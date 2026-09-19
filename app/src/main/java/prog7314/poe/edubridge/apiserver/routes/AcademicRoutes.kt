package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.*
import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Academic endpoints for results, attendance and timetable.
// GET /api/students/{id}/results
// GET /api/students/{id}/attendance
// GET /api/students/{id}/timetable

fun Route.academicRoutes() {
    route("/api/students/{id}") {

        get("/results") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/students/$id/results")
            call.respond(ApiDatabase.results.filter { it.studentId == id })
        }

        get("/attendance") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/students/$id/attendance")
            call.respond(ApiDatabase.attendance.filter { it.studentId == id })
        }

        get("/timetable") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/students/$id/timetable")
            call.respond(ApiDatabase.timetable.filter { it.studentId == id })
        }
    }
}