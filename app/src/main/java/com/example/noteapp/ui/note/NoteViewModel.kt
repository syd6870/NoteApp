package com.example.noteapp.ui.note

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.NoteDao

class NoteViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

}