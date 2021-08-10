package com.example.noteapp.ui.nearbyLocation

interface LocationUpdate {
    fun onLocationUpdateReceived(distance: String)
}