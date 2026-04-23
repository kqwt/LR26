package com.example.lr26.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object NotesList : Screen("notes_list")
    object NoteEdit : Screen("note_edit/{id}") {
        fun createRoute(id: String): String = "note_edit/$id"
    }
}