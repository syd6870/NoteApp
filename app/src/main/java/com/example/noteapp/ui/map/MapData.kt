package com.example.noteapp.ui.map

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapData(
    var latitude: Double,
    var longitude: Double,
    var address: String
) :Parcelable
