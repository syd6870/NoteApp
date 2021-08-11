package com.example.noteapp.extra

import kotlin.math.*

class Geo {

    companion object {
        private const val earthRadius = 6372.8 // In kilometers
    }


    fun getHaversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val originLat = Math.toRadians(lat1)
        val destLat = Math.toRadians(lat2)

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(lat1) * cos(lat2)
        val c = 2 * asin(sqrt(a))
        return earthRadius * c
    }


}

