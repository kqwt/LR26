package com.example.lr26.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr26.data.model.Note
import com.example.lr26.data.network.Resource
import com.example.lr26.data.repository.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NoteEditUiState(
    val noteId: String = "new",
    val title: String = "",
    val content: String = "",
    val isSaving: Boolean = false,
    val isLoading: Boolean = false
)

class NoteEditViewModel(private val repository: NotesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableStateFlow<Boolean?>(null)
    val navEvent: StateFlow<Boolean?> = _navEvent.asStateFlow()

    fun initNote(id: String) {
        _uiState.value = _uiState.value.copy(noteId = id, isLoading = true)

        if (id != "new") {
            viewModelScope.launch {
                // Заглушка - в реальной реализации загрузка с сервера
                kotlinx.coroutines.delay(300)
                _uiState.value = _uiState.value.copy(
                    title = "Загрузка...",
                    content = "Данные заметки",
                    isLoading = false
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun save() {
        if (_uiState.value.title.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)

            val note = Note(
                id = if (_uiState.value.noteId == "new")
                    java.util.UUID.randomUUID().toString()
                else _uiState.value.noteId,
                title = _uiState.value.title.trim(),
                content = _uiState.value.content.trim(),
                createdAt = System.currentTimeMillis(),
                userId = "user1"
            )

            when (repository.saveNote(note)) {
                is Resource.Success -> {
                    _navEvent.value = true
                }
                is Resource.Error -> {
                    // Обработка ошибки
                }
                is Resource.Loading -> {}
            }

            _uiState.value = _uiState.value.copy(isSaving = false)
        }
    }

    fun resetNavEvent() {
        _navEvent.value = null
    }
}