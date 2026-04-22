package com.example.lr26.data.local

import android.content.Context
import com.example.lr26.data.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotesCache(context: Context) {
    private val prefs = context.getSharedPreferences("notes_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        prefs.edit().putString("cached_notes", json).apply()
    }

    fun getNotes(): List<Note> {
        val json = prefs.getString("cached_notes", null) ?: return emptyList()
        val type = object : TypeToken<List<Note>>() {}.type
        return try { gson.fromJson(json, type) } catch (e: Exception) { emptyList() }
    }

    fun clear() { prefs.edit().remove("cached_notes").apply() }
}