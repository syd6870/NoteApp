package com.example.noteapp.ui.viewNote

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ViewNoteViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val noteEventChannel = Channel<NotesEvent>()
    val notesEvent = noteEventChannel.receiveAsFlow()

    val note = state.get<Note>("note")

    var noteTitle = state.get<String>("noteTitle") ?: note?.title ?: ""
        set(value) {
            field=value
            state.set("noteTitle",value)
        }

    var noteContent = state.get<String>("noteContent") ?: note?.content ?: ""
        set(value) {
            field=value
            state.set("noteContent",value)
        }

    var noteTime = state.get<String>("noteTime") ?: note?.createdTime ?: ""
        set(value) {
            field=value
            state.set("noteTime",value)
        }


    var noteDate = state.get<String>("noteDate") ?: note?.createdDate ?: ""
        set(value) {
            field=value
            state.set("noteDate",value)
        }

    var noteLocation = state.get<String>("noteLocation") ?: note?.address ?: ""
        set(value) {
            field=value
            state.set("noteLocation",value)
        }

    var noteTracked = state.get<Boolean>("noteTracked") ?: note?.isTracked ?: false
        set(value) {
            field=value
            state.set("noteTracked",value)
        }

    var noteLastEdit = state.get<Long>("noteLastEdit") ?: note?.lastEditOn ?: note?.created ?: System.currentTimeMillis()
        set(value) {
            field=value
            state.set("noteLastEdit",value)
        }

    private val noteTemp=note ?: Note("Empty")

    fun onEditNoteClick()=viewModelScope.launch{
        noteEventChannel.send(NotesEvent.NavigateToEditNoteScreen(noteTemp))
    }

    sealed class NotesEvent{
        data class NavigateToEditNoteScreen(val note:Note): NotesEvent()
    }
}