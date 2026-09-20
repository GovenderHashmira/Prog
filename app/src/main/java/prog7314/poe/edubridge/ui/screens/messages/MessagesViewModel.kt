package prog7314.poe.edubridge.ui.screens.messages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.model.Message
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.state.ScreenState

enum class MessageFilter { ALL, UNREAD }

data class MessagesData(
    val messages: List<Message>,
    val filter: MessageFilter,
    val unreadCount: Int
)

class MessagesViewModel : ViewModel() {

    var state by mutableStateOf<ScreenState<MessagesData>>(ScreenState.Loading)
        private set

    private var activeFilter = MessageFilter.ALL

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = ScreenState.Loading
            delay(400)
            state = build()
        }
    }

    fun selectFilter(filter: MessageFilter) {
        activeFilter = filter
        state = build()
    }

    private fun build(): ScreenState<MessagesData> {
        val all = SampleData.messages.sortedByDescending { it.sentAt }
        val visible = when (activeFilter) {
            MessageFilter.ALL -> all
            MessageFilter.UNREAD -> all.filter { it.isUnread }
        }
        val unreadCount = all.count { it.isUnread }
        return if (visible.isEmpty()) {
            ScreenState.Empty
        } else {
            ScreenState.Success(MessagesData(visible, activeFilter, unreadCount))
        }
    }

    fun retry() = load()
}
