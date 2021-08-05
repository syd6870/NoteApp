package com.example.noteapp.ui.note

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.data.PreferencesManager
import com.example.noteapp.data.SortOrder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class NoteViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state:SavedStateHandle
) : ViewModel() {

    private val taskEventChannel = Channel<NotesEvent>()
    val tasksEvent = taskEventChannel.receiveAsFlow()

    val searchQuery = state.getLiveData("searchQuery","")

    val preferenceFlow=preferencesManager.preferenceFlow

    private val taskFlow = combine(
        searchQuery.asFlow(),
        preferenceFlow
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (query, filterPreference) ->
            noteDao.getNotes(query, filterPreference.sortOrder,filterPreference.hideCompleted)
        }

    val notes = taskFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder)= viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }


    fun onHideCompletedClick(hideCompleted:Boolean)= viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onThemeSelected(theme:String)= viewModelScope.launch {
        preferencesManager.updateTheme(theme )
    }

    fun onNoteSelected(note: Note)=viewModelScope.launch{
        taskEventChannel.send(NotesEvent.NavigateToViewNoteScreen(note))
    }

    fun onAddNewNoteClick()=viewModelScope.launch {
        taskEventChannel.send(NotesEvent.NavigateToAddNoteScreen)
    }


    sealed class NotesEvent{
        object NavigateToAddNoteScreen :NotesEvent()
        data class NavigateToViewNoteScreen(val note:Note):NotesEvent()
    }


}

