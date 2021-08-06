package com.example.noteapp.ui.addEditNote

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.ui.ADD_NOTE_RESULT_OK
import com.example.noteapp.ui.EDIT_NOTE_RESULT_OK
import com.example.noteapp.util.toTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val TAG = "AddEditTaskViewModel"
    private val currentDatePlus1 = System.currentTimeMillis() + 86400000L;
    private val currentTime = System.currentTimeMillis();
    val note = state.get<Note>("note")


    var noteTitle = state.get<String>("noteTitle") ?: note?.title ?: ""
        set(value) {
            field = value
            state.set("noteTitle", value)
        }

    var noteContent = state.get<String>("noteContent") ?: note?.content ?: ""
        set(value) {
            field = value
            state.set("noteContent", value)
        }

    var noteRemindLong = state.get<Long>("noteReminderLong") ?: note?.remindOn ?: currentDatePlus1
        set(value) {
            field = value
            state.set("noteReminderLong", value)
        }

    var noteRemindTime =
        state.get<String>("noteReminderTime") ?: note?.reminderTime ?: currentDatePlus1.toTime()
        set(value) {
            field = value
            state.set("noteReminderTime", value)
        }

    var noteRemindDate =
        state.get<String>("noteReminderDate") ?: note?.reminderDate ?: currentDatePlus1.toTime()
        set(value) {
            field = value
            state.set("noteReminderDate", value)
        }

    var noteLocationlatitude =
        state.get<Float>("noteLocationLatitude") ?: note?.latitude ?: 0.0f
        set(value) {
            field = value
            state.set("noteLocationLatitude", value)
        }

    var noteLocationlongitude =
        state.get<Float>("noteLocationLongitude") ?: note?.longitude ?: 0.0f
        set(value) {
            field = value
            state.set("noteLocationLongitude", value)
        }

    var noteAddress = state.get<String>("noteAddress") ?: note?.address ?: ""
        set(value) {
            field = value
            state.set("noteAddress", value)
        }

    var noteTracked = state.get<Boolean>("noteTracked") ?: note?.isTracked ?: false
        set(value) {
            field = value
            state.set("noteTracked", value)
        }

    private val addEditNoteEventChannel = Channel<AddEditNoteEvent>()
    val addEditNoteEvent = addEditNoteEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (noteTitle.isBlank()) {
            showInvalidInputMessage("Title cannot be Empty")
            return
        }
        if (noteContent.isBlank()) {
            showInvalidInputMessage("Note body cannot be Empty")
            return
        }
        if (noteRemindLong < currentTime) {
            showInvalidInputMessage("Time already Passed")
            return
        }

        if (note != null) {
            val updatedNote = note.copy(
                title = noteTitle,
                content = noteContent,
                remindOn = noteRemindLong,
                latitude = noteLocationlatitude,
                longitude = noteLocationlongitude,
                address = noteAddress,
                lastEditOn = currentTime,
                isTracked = noteTracked
            )

            updateNote(updatedNote)
        } else {
            val note = Note(title = noteTitle)
            createNote(note)
        }
    }

    private fun createNote(note: Note) = viewModelScope.launch {
        noteDao.insert(note)
        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBackWithResult(ADD_NOTE_RESULT_OK))
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        noteDao.update(note)
        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBackWithResult(EDIT_NOTE_RESULT_OK))
    }

    private fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditNoteEventChannel.send(AddEditNoteEvent.showInvalidInputMessage(message))
    }

    sealed class AddEditNoteEvent {
        data class showInvalidInputMessage(val msg: String) : AddEditNoteEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditNoteEvent()
    }


}