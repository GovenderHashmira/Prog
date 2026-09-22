package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.ApiLogger
import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Academic endpoints for results, attendance and timetable.
 *
 * GET /api/students/{id}/results
 * GET /api/students/{id}/attendance
 * GET /api/students/{id}/timetable
 *
 * @author Member 2 — Khumo Machoga
 */
fun Route.academicRoutes() {

    route("/api/students/{id}") {

        // ── Results ─────────────────────────────────────
        get("/results") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/students/$id/results")
            val results = ApiDatabase.results.filter { it.studentId == id }
            call.respond(
                results.map { r ->
                    mapOf(
                        "resultId" to r.id,
                        "studentId" to r.studentId,
                        "subjectId" to r.subject.lowercase().replace(" ", "-"),
                        "subjectName" to r.subject,
                        "assessmentName" to r.assessmentName,
                        "score" to r.mark,
                        "academicPeriod" to r.period,
                        "teacherComment" to r.teacherComment,
                        "updatedAt" to r.updatedAt
                    )
                }
            )
        }

        // ── Attendance ──────────────────────────────────
        get("/attendance") {
            val id = call.parameters["id"]
            ApiLogger.d("GET /api/students/$id/attendance")
            val records = ApiDatabase.attendance.filter { it.studentId == id }
            call.respond(
                records.map { a ->
                    mapOf(
                        "attendanceId" to a.id,
                        "studentId" to a.studentId,
                        "date" to a.date,
                        "status" to a.status,
                        "comment" to a.comment
                    )
                }
            )
        }

        // ── Timetable ───────────────────────────────────
        // Returns TimetableDto shape (matches M3's Retrofit contract):
        //   { timetableId, studentId, academicYear, term, periods: [...] }
        get("/timetable") {
            val id = call.parameters["id"] ?: ""
            ApiLogger.d("GET /api/students/$id/timetable")

            val periods = ApiDatabase.timetable.filter { it.studentId == id }

            call.respond(
                mapOf(
                    "timetableId" to "tt-$id",
                    "studentId" to id,
                    "academicYear" to 2026,
                    "term" to 1,
                    "periods" to periods.mapIndexed { index, p ->
                        mapOf(
                            "periodId" to p.id,
                            "timetableId" to "tt-$id",
                            "subjectId" to p.subject.lowercase().replace(" ", "-"),
                            "subjectName" to p.subject,
                            "teacherName" to p.teacher,
                            "location" to p.room,
                            "dayOfWeek" to dayToNumber(p.day),
                            "startTime" to p.startTime,
                            "endTime" to p.endTime
                        )
                    }
                )
            )
        }
    }
}

/**
 * Convert day name from mock data to day number expected by ClassPeriodDto.
 * 1 = Monday … 7 = Sunday
 */
private fun dayToNumber(day: String): Int = when (day.lowercase()) {
    "monday"    -> 1
    "tuesday"   -> 2
    "wednesday" -> 3
    "thursday"  -> 4
    "friday"    -> 5
    "saturday"  -> 6
    "sunday"    -> 7
    else        -> 1
}