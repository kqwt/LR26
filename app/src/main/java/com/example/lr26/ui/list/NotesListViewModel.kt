package com.example.lr26.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr26.data.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotesListUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class NotesListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NotesListUiState())
    val uiState: StateFlow<NotesListUiState> = _uiState.asStateFlow()

    private val notesList = mutableListOf<Note>()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(500)

            if (notesList.isEmpty()) {
                notesList.addAll(
                    listOf(
                        Note("1", "ЛР", "Изучить MVVM, Compose, Navigation", System.currentTimeMillis() - 86400000),
                        Note("2", "Список покупок", "Молоко, хлеб, кофе, сахар", System.currentTimeMillis() - 172800000),
                        Note("3", "План на неделю", "Пн-Вт: лекции, Ср: лаба, Чт-Пт: доработка", System.currentTimeMillis() - 259200000)
                    )
                )
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                notes = notesList.toList()
            )
        }
    }

    fun addNote(note: Note) {
        notesList.add(0, note)
        _uiState.value = _uiState.value.copy(notes = notesList.toList())
    }

    fun updateNote(note: Note) {
        val index = notesList.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notesList[index] = note
            _uiState.value = _uiState.value.copy(notes = notesList.toList())
        }
    }

    fun deleteNote(id: String) {
        notesList.removeAll { it.id == id }
        _uiState.value = _uiState.value.copy(notes = notesList.toList())
    }
}