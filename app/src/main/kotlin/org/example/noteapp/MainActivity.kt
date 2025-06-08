package org.example.noteapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.example.noteapp.ui.screens.NoteEditorScreen
import org.example.noteapp.ui.screens.NoteListScreen
import org.example.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    private var repository: NoteRepository? = null
    private var isInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoteAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoading by remember { mutableStateOf(true) }
                    var error by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(Unit) {
                        try {
                            val database = NoteDatabase.getDatabase(applicationContext)
                            repository = NoteRepository(database.noteDao())
                            isInitialized = true
                            isLoading = false
                        } catch (e: Exception) {
                            error = e.message
                            isLoading = false
                        }
                    }

                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        error != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: $error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        isInitialized -> {
                            repository?.let { repo ->
                                AppContent(repository = repo)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppContent(repository: NoteRepository) {
    val navController = rememberNavController()
    val viewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(repository)
    )

    NavHost(navController = navController, startDestination = "notes") {
        composable("notes") {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { note ->
                    viewModel.setCurrentNote(note)
                    navController.navigate("editor")
                },
                onAddClick = {
                    viewModel.setCurrentNote(null)
                    navController.navigate("editor")
                }
            )
        }
        composable("editor") {
            NoteEditorScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 