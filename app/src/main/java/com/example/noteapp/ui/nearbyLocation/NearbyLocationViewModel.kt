package com.example.noteapp.ui.nearbyLocation

import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.extra.Geo
import com.example.noteapp.extra.MyLocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class NearbyLocationViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao
) : ViewModel() {
    private val TAG = "NearbyLocationViewModel"


    private val test= MutableStateFlow("")

    private var allNoteFlow =  noteDao.getAllTrackedNonCompletedNote().asLiveData()

   // private val testData=noteDao.test().asLiveData()

    var noteListLiveData = MutableLiveData<List<Note>>()
    init {
        allNoteFlow.observeForever {
            Log.d(TAG, "OBSERVER : ${it}")
           noteListLiveData.value= it
        }
    }



    var location = MutableLiveData<Pair<Double, Double>>()

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //val noteListLiveData = MutableLiveData<List<Note>>(allNoteFlow.asLiveData().value)


    private val geo = Geo()


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
            fusedLocationClient
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


}