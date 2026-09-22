package prog7314.poe.edubridge.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.repository.AuthRepository
import prog7314.poe.edubridge.util.Resource
import javax.inject.Inject

data class LoginUiState(
    val email: String = "sarah@edubridge.com",
    val password: String = "password123",
    val emailInvalid: Boolean = false,
    val passwordInvalid: Boolean = false,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isRegisterMode: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, emailInvalid = false, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordInvalid = false, errorMessage = null) }
    }

    fun toggleMode() {
        _uiState.update {
            it.copy(
                isRegisterMode = !it.isRegisterMode,
                errorMessage = null,
                emailInvalid = false,
                passwordInvalid = false
            )
        }
    }

    fun continueWithSso() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password

        var valid = true
        if (email.isEmpty() || !emailPattern.matches(email)) {
            _uiState.update { it.copy(emailInvalid = true) }
            valid = false
        }
        if (password.length < 6) {
            _uiState.update { it.copy(passwordInvalid = true) }
            valid = false
        }
        if (!valid) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            // Prototype: both "login" and "register" use the same repository call.
            // A full implementation would route registration to a separate API endpoint.
            val result = authRepository.login(
                provider = "local",
                identityToken = password,
                deviceId = "edubridge-android"
            )

            when (result) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Authentication failed"
                        )
                    }
                }
                is Resource.Loading -> Unit
                is Resource.Empty -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "No account found")
                    }
                }
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}