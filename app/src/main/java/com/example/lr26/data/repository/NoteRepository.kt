package com.example.lr26.data.repository

import com.example.lr26.data.api.NoteApi
import com.example.lr26.data.model.Note
import com.example.lr26.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepository(private val api: NoteApi) {

    suspend fun getNotes(): Resource<List<Note>> = withContext(Dispatchers.IO) {
        try {
            Resource.Success(api.getNotes())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ошибка сети")
        }
    }

    suspend fun saveNote(note: Note): Resource<Note> = withContext(Dispatchers.IO) {
        try {
            Resource.Success(api.createNote(note))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ошибка сохранения")
        }
    }

    suspend fun updateNote(id: String, note: Note): Resource<Note> = withContext(Dispatchers.IO) {
        try {
            Resource.Success(api.updateNote(id, note))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ошибка обновления")
        }
    }

    suspend fun deleteNote(id: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            api.deleteNote(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ошибка удаления")
        }
    }
}