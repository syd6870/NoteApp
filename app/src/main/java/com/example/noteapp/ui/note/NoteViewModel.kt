package com.example.noteapp.ui.note

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest


class NoteViewModel @ViewModelInject constructor(
    noteDao: NoteDao
) : ViewModel() {

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)
    val searchQuery = MutableStateFlow("")

    private val taskFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ) { query, sortOrder, hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }
        .flatMapLatest { (query, sortOrder, hideCompleted) ->
            noteDao.getNotes(query, sortOrder, hideCompleted)
        }

    val notes = taskFlow.asLiveData()
}

enum class SortOrder { BY_NAME, BY_DATE }