package org.example.noteapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.example.noteapp.data.Note
import org.example.noteapp.data.NoteRepository

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _searchResults = MutableStateFlow<List<Note>>(emptyList())
    val searchResults: StateFlow<List<Note>> = _searchResults

    init {
        viewModelScope.launch {
            repository.allNotes
                .catch { e -> e.printStackTrace() }
                .collect { notes ->
                    _notes.value = notes
                }
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            repository.searchNotes(query)
                .catch { e -> e.printStackTrace() }
                .collect { notes ->
                    _searchResults.value = notes
                }
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            val note = Note(title = title, content = content)
            repository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
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