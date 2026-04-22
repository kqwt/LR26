package com.example.lr26.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun login() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        if (_uiState.value.email.isNotBlank() && _uiState.value.password.length >= 4) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isLoggedIn = true
            )
        } else {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Введите email и пароль (мин. 4 символа)"
            )
        }
    }
}