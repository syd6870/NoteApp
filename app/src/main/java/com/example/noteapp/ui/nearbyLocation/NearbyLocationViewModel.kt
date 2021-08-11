package com.example.noteapp.ui.nearbyLocation

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.extra.Geo
import com.example.noteapp.extra.LocationError
import com.example.noteapp.extra.MyLocationService
import com.example.noteapp.ui.dialog.DialogData
import com.example.noteapp.ui.dialog.MyDialogListener
import com.example.noteapp.ui.note.NoteViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class NearbyLocationViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao
) : ViewModel() {
    private val TAG = "NearbyLocationViewModel"

    private var allNoteFlow = noteDao.getAllTrackedNonCompletedNote().asLiveData()
    var noteListLiveData = MutableLiveData<List<Note>>()

    init {
        allNoteFlow.observeForever {
            Log.d(TAG, "OBSERVER : ${it}")
            noteListLiveData.value = it
        }
    }


    private val locationEventChannel = Channel<LocationEvents>()
    val locationEvent = locationEventChannel.receiveAsFlow()

    //var location = MutableLiveData<Pair<Double, Double>>()

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val geo = Geo()

    private val locationErrorListener = object : LocationError {
        override fun resolvableError() {
            locationErrorReceived()
        }
    }

    //resolvable Error from Location Service
    fun locationErrorReceived() = viewModelScope.launch {
        locationEventChannel.send(LocationEvents.ShowToastMessage("Click on refresh option from top-right menu"))

    }

    fun startLocationUpdates(activity: Activity, context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d(TAG, "onLocationResult: ${location.latitude} ${location.longitude}")
                    Toast.makeText(
                        context,
                        " ${location.latitude} ${location.longitude}",
                        Toast.LENGTH_LONG
                    ).show()

                    Log.d(TAG, "onLocationResult: ${noteListLiveData.value.toString()}")
                    updateNoteDistance(noteListLiveData.value, location)


                }
            }
        }

        MyLocationService().createLocationRequest(
            activity,
            locationCallback,
            fusedLocationClient,
            locationErrorListener
        )
    }


    fun updateNoteDistance(noteList: List<Note>?, currentLocation: Location) {
        val resultList = mutableListOf<Note>()
        if (noteList != null) {
            for (note in noteList) {
                val distance =
                    geo.getHaversine(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        note.latitude.toDouble(),
                        note.longitude.toDouble()
                    ).toFloat()

                resultList.add(note.copy(distanceFromUser = distance))
            }

            resultList.sortBy { it.distanceFromUser }

            noteListLiveData.value = resultList
        }


    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    fun onMarkCompletedClick(note: Note) = viewModelScope.launch {

        val listener = object : MyDialogListener {
            override fun onButtonClick(positiveButtonClick: Boolean) {
                onMarkCompletedPositiveClick(note)
            }
        }
        val dialogData =
            DialogData("Mark Completed", "Mark this note as completed?", listener = listener)

        locationEventChannel.send(LocationEvents.NavigateToShowDialog(dialogData))
    }

    //click from dialog
    fun onMarkCompletedPositiveClick(note: Note) = viewModelScope.launch {
        noteDao.update(note.copy(isCompleted = true))
    }


    sealed class LocationEvents {
        data class ShowToastMessage(val msg: String) : LocationEvents()
        data class NavigateToShowDialog(val dialogData: DialogData) : LocationEvents()
    }

}