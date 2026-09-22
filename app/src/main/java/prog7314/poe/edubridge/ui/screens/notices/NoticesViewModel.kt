package prog7314.poe.edubridge.ui.screens.notices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.model.Notice
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.state.ScreenState

data class NoticesData(
    val notices: List<Notice>
)

class NoticesViewModel : ViewModel() {

    var state by mutableStateOf<ScreenState<NoticesData>>(ScreenState.Loading)
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = ScreenState.Loading
            delay(400)
            val notices = SampleData.notices
                .filter { !it.isExpired }
                .sortedByDescending { it.publishedAt }
            state = if (notices.isEmpty()) {
                ScreenState.Empty
            } else {
                ScreenState.Success(NoticesData(notices))
            }
        }
    }

    fun retry() = load()
}
