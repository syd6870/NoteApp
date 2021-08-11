package com.example.noteapp.extra

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import javax.inject.Singleton

@Singleton
class MyLocationService() {
    private val REQUEST_CHECK_SETTINGS: Int = 201


    fun createLocationRequest(
        activity: Activity,
        locationCallback: LocationCallback,
        fusedLocationClient: FusedLocationProviderClient,
        locationErrorCallback : LocationError
    ) {

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            startLocationUpdates(locationRequest, locationCallback, fusedLocationClient)
        }

        task.addOnFailureListener { exception ->
            Log.d("MyLocationService", "Main Exception ${exception.localizedMessage}")
            if (exception is ResolvableApiException) {
                Log.d("MyLocationService", "Resolvable Exception ${exception.localizedMessage}")
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_SETTINGS
                    )

                    locationErrorCallback.resolvableError()
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("MyLocationService", "Exception ${sendEx.localizedMessage}")
                    // Ignore the error.
                }
            } else {
                Log.d("MyLocationService", "Non Resolvable Exception ${exception.localizedMessage}")
            }

        }


    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback,
        fusedLocationClient: FusedLocationProviderClient
    ) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


}

interface LocationError {
    fun resolvableError()
}