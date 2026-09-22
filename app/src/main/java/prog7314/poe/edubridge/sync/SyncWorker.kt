package prog7314.poe.edubridge.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import prog7314.poe.edubridge.data.repository.SyncRepository
import prog7314.poe.edubridge.util.Resource

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(appContext, params) {

    private companion object {
        const val TAG = "EduBridge-Sync"
        const val KEY_DEVICE_ID = "deviceId"
    }

    override suspend fun doWork(): Result {
        val deviceId = inputData.getString(KEY_DEVICE_ID) ?: "unknown-device"
        Log.i(TAG, "SyncWorker started deviceId=$deviceId")

        return when (val result = syncRepository.syncAll(deviceId)) {
            is Resource.Success -> {
                Log.i(TAG, "Sync succeeded")
                Result.success()
            }
            is Resource.Empty -> {
                Log.d(TAG, "Sync skipped — no pending operations")
                Result.success()
            }
            is Resource.Error -> {
                Log.w(TAG, "Sync failed: ${result.message}")
                if (runAttemptCount < 3) Result.retry() else Result.failure()
            }
            Resource.Loading -> Result.retry()
        }
    }
}