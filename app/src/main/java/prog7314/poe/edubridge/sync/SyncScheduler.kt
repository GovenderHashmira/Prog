package prog7314.poe.edubridge.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        const val TAG = "EduBridge-Sync"
        const val WORK_NAME = "EduBridgePeriodicSync"
        const val KEY_DEVICE_ID = "deviceId"
        const val INTERVAL_MINUTES = 30L
    }

    private val workManager get() = WorkManager.getInstance(context)

    /** Schedule periodic sync while network is available. */
    fun schedulePeriodicSync(deviceId: String = "default-device") {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncWorker>(
            INTERVAL_MINUTES, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setInputData(workDataOf(KEY_DEVICE_ID to deviceId))
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
        Log.i(TAG, "Periodic sync scheduled every $INTERVAL_MINUTES min")
    }

    /** Trigger a one-off sync (e.g., on network reconnect). */
    fun runOnce(deviceId: String = "default-device") {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = androidx.work.OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(KEY_DEVICE_ID to deviceId))
            .build()

        workManager.enqueue(request)
        Log.d(TAG, "One-off sync enqueued")
    }

    fun cancel() {
        workManager.cancelUniqueWork(WORK_NAME)
        Log.d(TAG, "Periodic sync cancelled")
    }
}