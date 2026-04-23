package com.example.lr26.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr26.data.auth.TokenStorage
import kotlinx.coroutines.delay
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
    private val tokenStorage: TokenStorage
    // AuthApi удалён, так как используется заглушка.
    // В ЛР 28 или при подключении реального бэкенда верните: private val authApi: AuthApi
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
        // 1. Клиентская валидация
        if (_uiState.value.email.isBlank() || _uiState.value.password.length < 4) {
            _uiState.value = _uiState.value.copy(
                error = "Введите email и пароль (мин. 4 символа)",
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            // 2. Состояние загрузки
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // 3. ЗАГЛУШКА: имитация задержки сети (1.5 сек)
                delay(1500)

                // 4. Генерация фейкового токена
                val fakeToken = "stub_token_${System.currentTimeMillis()}"

                // 5. Сохранение в защищённое хранилище
                tokenStorage.saveToken(fakeToken)

                // 6. Успешный вход
                _uiState.value = _uiState.value.copy(isLoading = false)
                _authResult.value = true

            } catch (e: Exception) {
                // Обработка ошибок сохранения или непредвиденных сбоев
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        tokenStorage.clearToken()
        _authResult.value = false
    }

    fun resetAuthResult() {
        _authResult.value = null
    }
}