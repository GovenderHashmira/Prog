package prog7314.poe.edubridge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import prog7314.poe.edubridge.ui.navigation.EduBridgeNavGraph
import prog7314.poe.edubridge.ui.screens.settings.SettingsViewModel
import prog7314.poe.edubridge.ui.theme.EduBridgeTheme

// @AndroidEntryPoint is required for hiltViewModel() (used by LoginScreen) to work.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
            EduBridgeTheme(darkTheme = settings.darkModeEnabled) {
                EduBridgeNavGraph(settingsViewModel = settingsViewModel)
            }
        }
    }
}
