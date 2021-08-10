package com.example.noteapp.ui.folder

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.NoteDao
import com.example.noteapp.data.PreferencesManager
import com.example.noteapp.extra.MyAlarmManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class FolderFragmentViewModel @ViewModelInject constructor(
    private val myAlarmManager: MyAlarmManager
) : ViewModel() {

    private val folderEventChannel = Channel<FolderEvent>()
    val folderEvent = folderEventChannel.receiveAsFlow()


    fun onFolderSelected(folderName: String)=viewModelScope.launch{
        folderEventChannel.send(FolderEvent.NavigateToNoteScreen(folderName))
    }

    @Deprecated("Test Function. delete after use")
    fun setAlarm(){
        myAlarmManager.setAlarm(9768,System.currentTimeMillis()+3000)
    }

    sealed class FolderEvent{
        data class NavigateToNoteScreen(val folderName: String): FolderEvent()
    }
}