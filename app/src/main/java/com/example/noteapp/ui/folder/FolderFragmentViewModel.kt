package com.example.noteapp.ui.folder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class FolderFragmentViewModel : ViewModel() {

    private val folderEventChannel = Channel<FolderEvent>()
    val folderEvent = folderEventChannel.receiveAsFlow()


    fun onFolderSelected(folderName: String)=viewModelScope.launch{
        folderEventChannel.send(FolderEvent.NavigateToNoteScreen(folderName))
    }

    sealed class FolderEvent{
        data class NavigateToNoteScreen(val folderName: String): FolderEvent()
    }
}