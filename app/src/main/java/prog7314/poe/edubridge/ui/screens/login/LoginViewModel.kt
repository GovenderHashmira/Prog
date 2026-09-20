package prog7314.poe.edubridge.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val emailInvalid: Boolean = false,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, emailInvalid = false, errorMessage = null) }
    }

    fun continueWithSso() {
        val email = _uiState.value.email.trim()
        if (email.isEmpty() || !emailPattern.matches(email)) {
            _uiState.update { it.copy(emailInvalid = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true, emailInvalid = false, errorMessage = null) }
        viewModelScope.launch {
            delay(1200)
            _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
