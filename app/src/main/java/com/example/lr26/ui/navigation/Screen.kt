package com.example.lr26.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object NotesList : Screen("notes_list")
    object NoteEdit : Screen("note_edit/{noteId}") {
        fun createRoute(id: String = "new") = "note_edit/$id"
    }
}