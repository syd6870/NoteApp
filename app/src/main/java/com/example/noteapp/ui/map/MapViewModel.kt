package com.example.noteapp.ui.map

import android.location.Geocoder
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.LanguageCode
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.search.*
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

    //val geoCoderError = MutableLiveData(false)
    val internetAvailable = MutableLiveData(true)
    /*private val searchEngine = try {
        SearchEngine()
    } catch (e: InstantiationErrorException) {
        throw RuntimeException("Initialization of SearchEngine failed: " + e.error.name)

    }*/

    /*   private fun getAddressFromHereSearch(geoCoordinates: GeoCoordinates) {
           getAddressFromGeoCoder(geoCoordinates)
           val maxItems = 1
           val reverseGeocodingOptions = SearchOptions(LanguageCode.EN_GB, maxItems)
           mapData.latitude = geoCoordinates.latitude
           mapData.longitude = geoCoordinates.longitude
           try {
               searchEngine.search(geoCoordinates, reverseGeocodingOptions, addressSearchCallback)
           } catch (e: Exception) {
           }
       }*/

    /* private val addressSearchCallback =
         SearchCallback { searchError, list ->
             if (searchError != null) {
                 Log.d(TAG, "Address : GeoCoder ")
                 return@SearchCallback
             }

             // If error is null, list is guaranteed to be not empty.
             //showDialog("Reverse geocoded address:", list!![0].address.addressText)
             Log.d(TAG, "Address : SearchEngine ")
             mapData.address = list!![0].address.addressText
             enableButton.value = true
             val address = geocoder.getFromLocation(mapData.latitude, mapData.longitude, 1)
             Log.d(TAG, "GeoCoder : $address ")
             Log.d(TAG, "SearchEngine : ${list[0].address.addressText} ")
         }
 */

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

    /*  fun getAddress(geoCoordinates: GeoCoordinates, internetAvailable: Boolean) {
          if (internetAvailable)
              getAddressFromGeoCoder(geoCoordinates)
          else
              getAddressFromHereSearch(geoCoordinates)
      }*/


    sealed class MapEvent {
        data class NavigateBackFromMapWithResult(val result: MapData) :
            MapEvent()
    }
}