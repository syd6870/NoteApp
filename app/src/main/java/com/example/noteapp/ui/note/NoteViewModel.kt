package com.example.noteapp.ui.note

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest


class NoteViewModel @ViewModelInject constructor(
    noteDao: NoteDao
) : ViewModel() {

    val searchQuery= MutableStateFlow("")

    private val taskFlow=searchQuery.flatMapLatest {
        noteDao.getNotes(it)
    }

    val notes = taskFlow.asLiveData()
}