package com.example.lr26.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr26.data.api.AuthApi
import com.example.lr26.data.auth.TokenStorage
import com.example.lr26.data.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val tokenStorage: TokenStorage,
    private val authApi: AuthApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _authResult = MutableStateFlow<Boolean?>(null)
    val authResult: StateFlow<Boolean?> = _authResult.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun login() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.length < 4) {
            _uiState.value = _uiState.value.copy(
                error = "Заполните поля корректно",
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = authApi.login(_uiState.value.email, _uiState.value.password)) {
                is Resource.Success -> {
                    result.data?.let { token ->
                        tokenStorage.saveToken(token)
                        _authResult.value = true
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Ошибка авторизации"
                    )
                }
                is Resource.Loading -> {
                    // Уже установлено isLoading = true выше
                }
            }
        }
    }

    fun logout() {
        tokenStorage.clearToken()
        _authResult.value = false
    }
}