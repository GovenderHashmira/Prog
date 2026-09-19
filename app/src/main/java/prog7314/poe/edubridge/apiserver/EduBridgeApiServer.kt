package prog7314.poe.edubridge.apiserver

import prog7314.poe.edubridge.apiserver.routes.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

// EduBridge embedded REST API server.
// Runs inside the Android app on http://127.0.0.1:8080
// Implements all endpoints defined in POE Part 1.
// Start: EduBridgeApiServer.start() in MainActivity.onCreate()
// Stop:  EduBridgeApiServer.stop()  in MainActivity.onDestroy()

object EduBridgeApiServer {

    private var engine: ApplicationEngine? = null

    fun start() {
        if (engine != null) {
            ApiLogger.w("Server already running")
            return
        }

        ApiLogger.i("Starting EduBridge API on ${ApiConfig.BASE_URL}")

        engine = embeddedServer(
            factory = Netty,
            port = ApiConfig.PORT,
            host = ApiConfig.HOST
        ) {
            module()
        }.start(wait = false)

        ApiLogger.i("EduBridge API started on port ${ApiConfig.PORT}")
    }

    fun stop() {
        engine?.stop(1000, 3000)
        engine = null
        ApiLogger.i("EduBridge API stopped")
    }

    private fun Application.module() {
        install(DefaultHeaders)
        install(CallLogging)

        install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }

        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
        }

        install(StatusPages) {
            exception<Throwable> { call, cause ->
                ApiLogger.e("Unhandled error: ${cause.message}", cause)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (cause.message ?: "Unknown error"))
                )
            }
            status(HttpStatusCode.NotFound) { call, status ->
                call.respond(status, mapOf("error" to "Endpoint not found"))
            }
        }

        routing {
            authRoutes()
            userRoutes()
            studentRoutes()
            academicRoutes()
            communicationRoutes()
            settingsRoutes()

            get("/") {
                call.respond(
                    mapOf(
                        "name" to "EduBridge API",
                        "version" to "1.0",
                        "status" to "running",
                        "endpoints" to listOf(
                            "POST /api/auth/login",
                            "POST /api/auth/logout",
                            "GET  /api/users/me",
                            "GET  /api/users/me/settings",
                            "PUT  /api/users/me/settings",
                            "GET  /api/students",
                            "GET  /api/students/{id}",
                            "GET  /api/students/{id}/results",
                            "GET  /api/students/{id}/attendance",
                            "GET  /api/students/{id}/timetable",
                            "GET  /api/notices",
                            "GET  /api/messages",
                            "GET  /api/messages/{id}"
                        )
                    )
                )
            }
        }

        ApiLogger.i("All routes registered")
    }
}