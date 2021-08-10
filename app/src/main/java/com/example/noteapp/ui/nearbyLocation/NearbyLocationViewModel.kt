package com.example.noteapp.ui.nearbyLocation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NearbyLocationViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    val note = noteDao.getAllTrackedNonCompletedNote()
    var location = MutableLiveData<Pair<Double, Double>>()




}