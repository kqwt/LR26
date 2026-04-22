package com.example.lr26.data.repository
import com.example.lr26.data.api.NoteApi
import com.example.lr26.data.model.Note

class NoteRepository(private val api: NoteApi) {
    suspend fun getNotes(): List<Note> = api.getNotes()
    suspend fun saveNote(note: Note): Note = api.createNote(note)
    suspend fun deleteNote(id: String) = api.deleteNote(id)
}