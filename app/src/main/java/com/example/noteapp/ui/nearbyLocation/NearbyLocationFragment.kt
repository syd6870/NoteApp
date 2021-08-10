package com.example.noteapp.ui.nearbyLocation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.noteapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyLocationFragment : Fragment(R.layout.fragment_nearby_location) {
    private val viewModel:NearbyLocationViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}