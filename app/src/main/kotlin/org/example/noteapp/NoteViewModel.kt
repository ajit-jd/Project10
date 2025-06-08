package org.example.noteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allNotes.collect { notes ->
                _notes.value = notes
            }
        }
    }

    fun setCurrentNote(note: Note?) {
        _currentNote.value = note
    }

    fun createNote(title: String, content: String) {
        viewModelScope.launch {
            val now = Date()
            val note = Note(
                title = title,
                content = content,
                createdAt = now,
                updatedAt = now
            )
            repository.insertNote(note)
        }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            val currentNote = _currentNote.value
            val note = Note(
                id = id,
                title = title,
                content = content,
                createdAt = currentNote?.createdAt ?: Date(),
                updatedAt = Date()
            )
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            repository.searchNotes(query).collect { notes ->
                _notes.value = notes
            }
        }
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 