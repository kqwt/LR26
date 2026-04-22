package com.example.lr26.ui.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    viewModel: NoteEditViewModel = viewModel(),
    onBack: () -> Unit,
    onSaveNote: (Boolean) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.noteId == "new") "Новая заметка" else "Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = {
                    viewModel.updateTitle(it)
                    viewModel.clearError()
                },
                label = { Text("Заголовок *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.error != null
            )

            OutlinedTextField(
                value = state.content,
                onValueChange = { viewModel.updateContent(it) },
                label = { Text("Содержимое") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines = 10
            )

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val savedNote = viewModel.saveNote()
                        if (savedNote != null) {
                            onSaveNote(state.noteId == "new")
                        }
                    },
                    enabled = !state.isSaving && !state.isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (state.isSaving) "Сохранение..." else "Сохранить")
                }

                OutlinedButton(
                    onClick = onBack,
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отмена")
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}