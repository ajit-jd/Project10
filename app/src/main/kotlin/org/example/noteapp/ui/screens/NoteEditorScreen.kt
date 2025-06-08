package org.example.noteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.noteapp.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteViewModel,
    onNavigateBack: () -> Unit
) {
    val currentNote by viewModel.currentNote.collectAsState()
    var title by remember { mutableStateOf(currentNote?.title ?: "") }
    var content by remember { mutableStateOf(currentNote?.content ?: "") }

    LaunchedEffect(currentNote) {
        title = currentNote?.title ?: ""
        content = currentNote?.content ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentNote == null) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (currentNote != null) {
                        IconButton(
                            onClick = {
                                currentNote?.let { viewModel.deleteNote(it) }
                                onNavigateBack()
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (currentNote == null) {
                        viewModel.createNote(title, content)
                    } else {
                        viewModel.updateNote(currentNote!!.id, title, content)
                    }
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (currentNote == null) "Create" else "Save")
            }
        }
    }
} 