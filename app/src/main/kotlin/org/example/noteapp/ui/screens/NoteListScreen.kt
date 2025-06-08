package org.example.noteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import android.widget.Toast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.noteapp.Note
import org.example.noteapp.NoteViewModel
import org.example.noteapp.ui.components.NoteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit,
    onAddClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val notes by viewModel.notes.collectAsState()

    Scaffold(
        containerColor = Color(0xFF000080), // Navy blue background
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notes",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note")
                    }
                }
            )
        }
    ) { paddingValues ->
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply overall padding from Scaffold
        ) {
            Column(
                modifier = Modifier.fillMaxSize() // Column takes full size of the Box
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        viewModel.searchNotes(query)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search notes") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    shape = RoundedCornerShape(28.dp), // Rounded corners
                    singleLine = true
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(), // LazyColumn takes remaining space
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp), // Added bottom padding for FABs
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onClick = onNoteClick
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                text = { Text("New Note", color = Color.White) },
                onClick = { onAddClick() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp), // Standard padding
                containerColor = MaterialTheme.colorScheme.primary
            )

            ExtendedFloatingActionButton(
                text = { Text("Clip Webpage", color = Color.White) },
                onClick = {
                    Toast.makeText(context, "Clip Webpage clicked", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp), // Standard padding
                containerColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
} 