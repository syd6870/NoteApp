package com.example.noteapp.ui.note

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.NoteDao

class NoteViewModel @ViewModelInject constructor(
    noteDao: NoteDao
) : ViewModel() {
    val notes = noteDao.getNotes().asLiveData()
}