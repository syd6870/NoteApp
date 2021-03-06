package com.example.noteapp.ui.map

import android.location.Geocoder
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class MapViewModel @ViewModelInject constructor() : ViewModel() {

    private val TAG = "MapViewModel"
    val enableButton = MutableLiveData(false)
    private var mapData = MapData(400.0, 400.0, "")
    lateinit var geocoder: Geocoder

    private val mapEventChannel = Channel<MapEvent>()
    val mapEvent = mapEventChannel.receiveAsFlow()
    val internetAvailable = MutableLiveData(true)


    fun getAddressFromGeoCoder(geoCoordinates: GeoCoordinates) {
        if (internetAvailable.value == true) {
            val address =
                geocoder.getFromLocation(geoCoordinates.latitude, geoCoordinates.longitude, 1)
            mapData.address = address[0].getAddressLine(0)
            mapData.latitude = geoCoordinates.latitude
            mapData.longitude = geoCoordinates.longitude
            enableButton.value = true
        }
    }

    fun onConfirmButtonClick() = viewModelScope.launch {
        Log.d(TAG, "onConfirmButtonClick: $mapData")
        mapEventChannel.send(MapEvent.NavigateBackFromMapWithResult(mapData))
    }


    sealed class MapEvent {
        data class NavigateBackFromMapWithResult(val result: MapData) :
            MapEvent()
    }
}