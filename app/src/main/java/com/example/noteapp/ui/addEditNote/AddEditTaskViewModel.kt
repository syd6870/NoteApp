package com.example.noteapp.ui.addEditNote

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao

class AddEditTaskViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val TAG = "AddEditTaskViewModel"

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



}