package com.example.noteapp.ui.deleteAllCompleted

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.di.ApplicationScope
import com.example.noteapp.ui.addEditNote.AddEditNoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    @Assisted private val state: SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {


    val note = state.get<Note>("note")

    val message =
        if (note == null) {
            "Do you really want to delete all completed task"
        } else {
            "Delete this note?"
        }

    val resultMessage = if (note == null) {
        "Deleted all completed task and notes"
    } else {
        "Note deleted"
    }


    fun onConfirmClick() = applicationScope.launch {
        if (note != null) {
            noteDao.delete(note)
        } else
            noteDao.deleteCompletedNotes()

    }


}