package prog7314.poe.edubridge

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import prog7314.poe.edubridge.apiserver.EduBridgeApiServer
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltAndroidApp
class EduBridgeApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // The Retrofit client talks to the embedded Ktor server on 127.0.0.1:8080,
        // so it must be running for the whole app lifetime (not tied to an Activity,
        // which would stop it on every rotation). Started off the main thread
        // because binding the socket is blocking.
        thread(name = "EduBridgeApiServer", isDaemon = true) {
            try {
                EduBridgeApiServer.start()
            } catch (t: Throwable) {
                Log.e("EduBridgeApp", "Failed to start embedded API server", t)
            }
        }
    }
}
