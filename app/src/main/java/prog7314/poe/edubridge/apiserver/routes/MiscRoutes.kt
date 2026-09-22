package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.ApiLogger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant

/**
 * Sync, device registration and school endpoints.
 * @author Member 2
 */
fun Route.miscRoutes() {

    // POST /api/sync — offline sync batch
    post("/api/sync") {
        val body = call.receive<Map<String, Any>>()
        ApiLogger.d("POST /api/sync")
        call.respond(
            mapOf(
                "accepted" to 1,
                "rejected" to 0,
                "serverTimestamp" to Instant.now().toString(),
                "conflicts" to emptyList<String>()
            )
        )
    }

    // GET /api/sync/status
    get("/api/sync/status") {
        ApiLogger.d("GET /api/sync/status")
        call.respond(
            mapOf(
                "lastSyncAt" to Instant.now().toString(),
                "pendingCount" to 0,
                "serverTime" to Instant.now().toString()
            )
        )
    }

    // POST /api/devices/register
    post("/api/devices/register") {
        val body = call.receive<Map<String, String>>()
        ApiLogger.i("POST /api/devices/register platform=${body["platform"]}")
        call.respond(mapOf("registered" to true))
    }

    // GET /api/schools/{id}
    get("/api/schools/{id}") {
        val id = call.parameters["id"]
        ApiLogger.d("GET /api/schools/$id")
        call.respond(
            mapOf(
                "schoolId" to (id ?: "sch-1"),
                "name" to "EduBridge High School",
                "address" to "123 School Street, Johannesburg",
                "latitude" to -26.2041,
                "longitude" to 28.0473,
                "phone" to "+27 11 123 4567",
                "email" to "info@edubridge.co.za"
            )
        )
    }
}