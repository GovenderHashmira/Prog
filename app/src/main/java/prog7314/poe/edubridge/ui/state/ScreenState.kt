package prog7314.poe.edubridge.ui.state

sealed interface ScreenState<out T> {
    data object Loading : ScreenState<Nothing>
    data class Success<T>(val data: T) : ScreenState<T>
    data object Empty : ScreenState<Nothing>
    data class Error(val message: String) : ScreenState<Nothing>
}
