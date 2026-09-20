package prog7314.poe.edubridge.ui.screens.sync

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SyncUiState(
    val lastSyncedMinutesAgo: Int = 4,
    val storageUsedMb: Double = 12.4,
    val autoSyncEnabled: Boolean = true,
    val wifiOnlyEnabled: Boolean = false,
    val isSyncing: Boolean = false
)

class SyncViewModel : ViewModel() {

    var uiState by mutableStateOf(SyncUiState())
        private set

    fun syncNow() {
        if (uiState.isSyncing) return
        viewModelScope.launch {
            uiState = uiState.copy(isSyncing = true)
            delay(1600)
            uiState = uiState.copy(
                isSyncing = false,
                lastSyncedMinutesAgo = 0,
                storageUsedMb = uiState.storageUsedMb
            )
        }
    }

    fun setAutoSync(enabled: Boolean) {
        uiState = uiState.copy(autoSyncEnabled = enabled)
    }

    fun setWifiOnly(enabled: Boolean) {
        uiState = uiState.copy(wifiOnlyEnabled = enabled)
    }
}
