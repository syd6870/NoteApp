package com.example.noteapp.ui.dialogFragment

import android.app.DatePickerDialog
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.NoteDao
import com.example.noteapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope

class DatePickerViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    val listener=state.get<DatePickerDialog.OnDateSetListener>("dateListener")





}