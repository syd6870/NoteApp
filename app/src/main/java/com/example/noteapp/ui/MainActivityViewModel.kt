package com.example.noteapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.PreferencesManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
    preferencesManager: PreferencesManager,
) : ViewModel() {


    private val activityEventChannel = Channel<ActivityEvent>()
    val activityEvent = activityEventChannel.receiveAsFlow();
    private val preferenceFlow = preferencesManager.preferenceFlow

    init {
        setTheme()
    }

    private fun setTheme() = viewModelScope.launch {
        val theme=preferenceFlow.first().theme
        activityEventChannel.send(ActivityEvent.SetTheme(theme))
    }


    sealed class ActivityEvent {
        data class SetTheme(val theme: String) : ActivityEvent()
    }
}