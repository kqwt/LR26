package com.example.lr26.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr26.data.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class NoteEditUiState(
    val noteId: String = "new",
    val title: String = "",
    val content: String = "",
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class NoteEditViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    private val existingNotes = mutableMapOf<String, Note>()

    fun initNoteId(id: String) {
        _uiState.value = _uiState.value.copy(noteId = id, isLoading = true)

        if (id != "new") {
            viewModelScope.launch {
                kotlinx.coroutines.delay(300)
                val existingNote = existingNotes[id]
                if (existingNote != null) {
                    _uiState.value = _uiState.value.copy(
                        title = existingNote.title,
                        content = existingNote.content,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        title = "Заметка $id",
                        content = "Содержимое заметки",
                        isLoading = false
                    )
                }
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

    fun saveNote(): Note? {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.value = state.copy(error = "Заголовок не может быть пустым")
            return null
        }

        _uiState.value = state.copy(isSaving = true, error = null)

        val note = Note(
            id = if (state.noteId == "new") UUID.randomUUID().toString() else state.noteId,
            title = state.title.trim(),
            content = state.content.trim(),
            createdAt = System.currentTimeMillis(),
            isFavorite = false,
            userId = "user1"
        )

        existingNotes[note.id] = note

        viewModelScope.launch {
            kotlinx.coroutines.delay(500)
            _uiState.value = _uiState.value.copy(isSaving = false)
        }

        return note
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}