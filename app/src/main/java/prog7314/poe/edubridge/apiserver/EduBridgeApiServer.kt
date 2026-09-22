package prog7314.poe.edubridge.apiserver

import prog7314.poe.edubridge.apiserver.routes.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeoutOrNull

// EduBridge embedded REST API server.
// Runs inside the Android app on http://127.0.0.1:8080
// Implements all endpoints defined in POE Part 1.
// Started once from EduBridgeApplication.onCreate() on a background thread.
// Uses the CIO engine: pure Kotlin, and far more reliable on Android than Netty.

object EduBridgeApiServer {

    private var engine: ApplicationEngine? = null

    private val ready = CompletableDeferred<Unit>()

    /** Set if the server failed to start, so the UI can show the real reason. */
    @Volatile
    var startupError: Throwable? = null
        private set

    /**
     * Suspends until the server is accepting connections (it starts on a background
     * thread, and on a slow emulator that can take longer than the splash screen).
     * Returns false if it failed to start or didn't start within [timeoutMs].
     */
    suspend fun awaitReady(timeoutMs: Long = 8_000): Boolean = try {
        withTimeoutOrNull(timeoutMs) { ready.await(); true } ?: false
    } catch (e: Throwable) {
        false
    }

    @Synchronized
    fun start() {
        if (engine != null) {
            ApiLogger.w("Server already running")
            return
        }

        ApiLogger.i("Starting EduBridge API on ${ApiConfig.BASE_URL}")

        try {
            engine = embeddedServer(
                factory = CIO,
                port = ApiConfig.PORT,
                host = ApiConfig.HOST
            ) {
                module()
            }.start(wait = false)
        } catch (t: Throwable) {
            startupError = t
            ready.completeExceptionally(t)
            ApiLogger.e("EduBridge API failed to start", t)
            throw t
        }
        ready.complete(Unit)

        ApiLogger.i("EduBridge API started on port ${ApiConfig.PORT}")
    }

    @Synchronized
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
            miscRoutes()

            get("/") {
                call.respond(
                    mapOf(
                        "name" to "EduBridge API",
                        "version" to "1.0",
                        "status" to "running",
                        "endpoints" to listOf(
                            "POST /api/auth/sso",
                            "POST /api/auth/login",
                            "POST /api/auth/register",
                            "POST /api/auth/refresh",
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
                            "GET  /api/messages/{id}",
                            "POST /api/sync",
                            "GET  /api/sync/status",
                            "POST /api/devices/register",
                            "GET  /api/schools/{id}"
                        )
                    )
                )
            }
        }

        ApiLogger.i("All routes registered")
    }
}