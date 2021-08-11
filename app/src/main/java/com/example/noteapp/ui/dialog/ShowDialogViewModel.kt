package com.example.noteapp.ui.dialog

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope

class ShowDialogViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    @Assisted private val state: SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {


    val dialogData = state.get<DialogData>("dialogData")!!
}