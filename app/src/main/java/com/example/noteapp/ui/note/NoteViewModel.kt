package com.example.noteapp.ui.note

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.data.PreferencesManager
import com.example.noteapp.data.SortOrder
import com.example.noteapp.ui.ADD_NOTE_RESULT_OK
import com.example.noteapp.ui.EDIT_NOTE_RESULT_OK
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

    private val noteEventChannel = Channel<NotesEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()

    private val folderName=state.get<String>("title") ?: "Note"
    val searchQuery = state.getLiveData("searchQuery","")

    val preferenceFlow=preferencesManager.preferenceFlow

    private val taskFlow = combine(
        searchQuery.asFlow(),
        preferenceFlow
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (query, filterPreference) ->
            noteDao.getNotes(query, filterPreference.sortOrder,filterPreference.hideCompleted,folderName)
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
        noteEventChannel.send(NotesEvent.RecreateActivity)
    }

    fun onNoteSelected(note: Note)=viewModelScope.launch{
        noteEventChannel.send(NotesEvent.NavigateToViewNoteScreen(note))
    }

    fun onAddNewNoteClick()=viewModelScope.launch {
        noteEventChannel.send(NotesEvent.NavigateToAddNoteScreen)
    }

    fun onAddEditResult(result:Int){
        when (result) {
            ADD_NOTE_RESULT_OK->showNoteSavedConfirmationMessage("Note Added")
            EDIT_NOTE_RESULT_OK->showNoteSavedConfirmationMessage("Note Updated")
        }
    }


    fun showNoteSavedConfirmationMessage(msg:String)=viewModelScope.launch {
        noteEventChannel.send(NotesEvent.ShowNoteSavedConfirmationMessage(msg))
    }

    fun onDeleteAllCompletedClick()=viewModelScope.launch {
        noteEventChannel.send(NotesEvent.NavigateToDeleteAllCompleteScreen(null))
    }

    fun onDeleteResult(result:String?)=viewModelScope.launch{
        noteEventChannel.send(NotesEvent.ShowNoteDeletedConfirmationMessage(result!!))
    }

    sealed class NotesEvent{
        object NavigateToAddNoteScreen :NotesEvent()
        data class NavigateToViewNoteScreen(val note:Note):NotesEvent()
        data class ShowNoteSavedConfirmationMessage(val msg:String):NotesEvent()
        data class NavigateToDeleteAllCompleteScreen(val note:Note?):NotesEvent()
        object RecreateActivity:NotesEvent()
        data class ShowNoteDeletedConfirmationMessage(val msg:String):NotesEvent()
    }


}

