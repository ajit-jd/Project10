package org.example.noteapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    notes: List<Note> = emptyList(),
    onNewNote: () -> Unit = {},
    onEditNote: (Note) -> Unit = {},
    onDeleteNote: (Note) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Filter notes based on search query
    val filteredNotes = notes.filter { note ->
        searchQuery.isEmpty() || 
        note.title.contains(searchQuery, ignoreCase = true) ||
        note.description.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
                navigationIcon = {
                    IconButton(onClick = { onMenuClick() }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { onNewNote() }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Note")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNewNote() }) {
                Icon(Icons.Filled.Add, contentDescription = "New Note")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search notes") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true
            )
            
            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) "No notes yet" else "No notes found",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Gray
                    )
                }
            } else {
                // Notes list
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredNotes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(note.title, style = MaterialTheme.typography.headlineSmall)
                                    Row {
                                        IconButton(onClick = { onEditNote(note) }) {
                                            Icon(Icons.Filled.Edit, contentDescription = "Edit Note")
                                        }
                                        IconButton(onClick = { onDeleteNote(note) }) {
                                            Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(note.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
} 