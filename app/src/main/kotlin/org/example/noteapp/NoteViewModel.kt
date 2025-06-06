package org.example.noteapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    init {
        val database = NoteDatabase.getDatabase(application)
        repository = NoteRepository(database.noteDao())
        viewModelScope.launch {
            repository.getAllNotes().collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    fun createNewNote() {
        val newNote = Note(
            title = "New Note",
            description = "",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.insert(newNote)
        }
    }

    fun editNote(note: Note) {
        _currentNote.value = note
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
            _currentNote.value = null
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
} 