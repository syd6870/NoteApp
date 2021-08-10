package com.example.noteapp.ui.nearbyLocation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.extra.MyLocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

private const val TAG = "NearbyLocationViewModel"

class NearbyLocationViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    val note = noteDao.getAllTrackedNonCompletedNote()
    var location = MutableLiveData<Pair<Double, Double>>()

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    fun startLocationUpdates(activity:Activity,context:Context){
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
                }
            }
        }

        MyLocationService().createLocationRequest(
            activity,
            locationCallback,
            fusedLocationClient
        )
    }

    fun stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



}