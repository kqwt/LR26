package com.example.lr26.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lr26.data.api.AuthApi
import com.example.lr26.data.api.NoteApi
import com.example.lr26.data.auth.TokenStorage
import com.example.lr26.data.local.NotesCache
import com.example.lr26.data.repository.NotesRepository
import com.example.lr26.ui.auth.AuthScreen
import com.example.lr26.ui.auth.AuthViewModel
import com.example.lr26.ui.edit.NoteEditScreen
import com.example.lr26.ui.edit.NoteEditViewModel
import com.example.lr26.ui.list.NotesListScreen
import com.example.lr26.ui.list.NotesListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    tokenStorage: TokenStorage,
    authApi: AuthApi,
    noteApi: NoteApi,
    notesCache: NotesCache
) {
    val isAuth = tokenStorage.getToken() != null
    val start = if (isAuth) Screen.NotesList.route else Screen.Auth.route

    val notesRepo = remember { NotesRepository(noteApi) }

    NavHost(navController = navController, startDestination = start) {
        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = viewModel { AuthViewModel(tokenStorage) }
            ) {
                navController.navigate(Screen.NotesList.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            }
        }

        composable(Screen.NotesList.route) {
            NotesListScreen(
                viewModel = viewModel { NotesListViewModel(notesRepo) },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.NoteEdit.createRoute(id))
                },
                onLogout = {
                    tokenStorage.clearToken()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.NoteEdit.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("id") ?: "new"
            val editViewModel: NoteEditViewModel = viewModel {
                NoteEditViewModel(notesRepo)
            }

            LaunchedEffect(noteId) {
                editViewModel.initNote(noteId)
            }

            NoteEditScreen(
                viewModel = editViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}