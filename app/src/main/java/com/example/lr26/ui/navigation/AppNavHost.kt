package com.example.lr26.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lr26.data.auth.TokenStorage
import com.example.lr26.ui.auth.AuthScreen
import com.example.lr26.ui.auth.AuthViewModel
import com.example.lr26.ui.edit.NoteEditScreen
import com.example.lr26.ui.list.NotesListScreen
import com.example.lr26.ui.list.NotesListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    tokenStorage: TokenStorage,
    authViewModel: AuthViewModel,
    notesViewModel: NotesListViewModel
) {
    val isAuthorized = tokenStorage.getToken() != null
    val startDest = if (isAuthorized) Screen.NotesList.route else Screen.Auth.route

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screen.Auth.route) {
            AuthScreen(viewModel = authViewModel) { success ->
                if (success) navController.navigate(Screen.NotesList.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            }
        }
        composable(Screen.NotesList.route) {
            NotesListScreen(
                viewModel = notesViewModel,
                onNavigateToEdit = { id -> navController.navigate(Screen.NoteEdit.createRoute(id)) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Auth.route) { popUpTo(0) { inclusive = true } }
                }
            )
        }
        composable(Screen.NoteEdit.route) { backStack ->
            val noteId = backStack.arguments?.getString("noteId") ?: "new"
            NoteEditScreen(
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}