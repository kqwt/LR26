package com.example.lr26.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lr26.ui.auth.AuthScreen
import com.example.lr26.ui.auth.AuthViewModel
import com.example.lr26.ui.edit.NoteEditScreen
import com.example.lr26.ui.edit.NoteEditViewModel
import com.example.lr26.ui.list.NotesListScreen
import com.example.lr26.ui.list.NotesListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val notesListViewModel: NotesListViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.NotesList.route) {
                        popUpTo(Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.NotesList.route) {
            NotesListScreen(
                viewModel = notesListViewModel,
                onNavigateToEdit = { noteId ->
                    navController.navigate(Screen.NoteEdit.createRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.NoteEdit.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    defaultValue = "new"
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "new"
            val editViewModel: NoteEditViewModel = viewModel()

            LaunchedEffect(noteId) {
                editViewModel.initNoteId(noteId)
            }

            NoteEditScreen(
                viewModel = editViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onSaveNote = { isNewNote ->
                    editViewModel.uiState.value.let { state ->
                        if (state.title.isNotBlank()) {
                            val note = com.example.lr26.data.model.Note(
                                id = if (isNewNote) java.util.UUID.randomUUID().toString() else noteId,
                                title = state.title,
                                content = state.content,
                                createdAt = System.currentTimeMillis(),
                                isFavorite = false,
                                userId = "user1"
                            )

                            if (isNewNote) {
                                notesListViewModel.addNote(note)
                            } else {
                                notesListViewModel.updateNote(note)
                            }
                        }
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}