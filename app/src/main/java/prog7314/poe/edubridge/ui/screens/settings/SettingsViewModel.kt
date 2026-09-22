package prog7314.poe.edubridge.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.model.Settings
import prog7314.poe.edubridge.ui.sample.SampleData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SettingsPreferences(application)
    private val userId = SampleData.currentUser.id

    val settings: StateFlow<Settings> = preferences.observe(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings.default(userId)
        )

    fun setLanguage(code: String) {
        viewModelScope.launch { preferences.setLanguage(code) }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch { preferences.setNotifications(enabled) }
    }

    fun setBiometric(enabled: Boolean) {
        viewModelScope.launch { preferences.setBiometric(enabled) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { preferences.setDarkMode(enabled) }
    }
}
