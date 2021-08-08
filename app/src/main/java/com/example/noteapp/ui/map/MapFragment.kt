package com.example.noteapp.ui.map

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentMapBinding
import com.example.noteapp.util.exhaustive
import com.here.sdk.mapviewlite.MapStyle
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapviewlite.MapImageFactory
import com.here.sdk.mapviewlite.MapMarker
import com.here.sdk.mapviewlite.MapMarkerImageStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import com.here.sdk.core.Anchor2D





@AndroidEntryPoint
class MapFragment() : Fragment(R.layout.fragment_map) {
    private val TAG = "MapFragment"
    private val viewModel: MapViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapImage = MapImageFactory.fromResource(view.resources, R.drawable.placeholder_64);
        var mapMarker = MapMarker(GeoCoordinates(19.0760, 72.8777));
        viewModel.geocoder = Geocoder(view.context)


        val binding = FragmentMapBinding.bind(view)
        binding.apply {
            mapView.onCreate(savedInstanceState)

            mapView.mapScene.loadScene(MapStyle.NORMAL_DAY) { errorCode ->
                if (errorCode == null) {
                    mapView.camera.target = GeoCoordinates(19.0760, 72.8777)
                    mapView.camera.zoomLevel = 14.0
                } else {
                    Log.d(TAG, "onLoadScene failed: $errorCode")
                }
            }

            val mapMarkerImageStyle = MapMarkerImageStyle()
            mapMarkerImageStyle.anchorPoint = Anchor2D(0.5, 1.0)
            mapMarker.addImage(mapImage, mapMarkerImageStyle);

            mapView.gestures.setTapListener { touchPoint ->
                val geoCoordinates = mapView.camera.viewToGeoCoordinates(touchPoint)

                mapMarker.coordinates = geoCoordinates
                mapView.mapScene.addMapMarker(mapMarker)
                Log.d(TAG, "Tap at: ${geoCoordinates.latitude} ${geoCoordinates.longitude}");
                viewModel.enableButton.value=false
                viewModel.getAddressFromCoordinates(geoCoordinates)
            }

            viewModel.enableButton.observe(viewLifecycleOwner) {
                buttonConfirmMapLocation.isEnabled = it
            }


            buttonConfirmMapLocation.setOnClickListener {
                viewModel.onConfirmButtonClick()
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mapEvent.collect { event ->
                when (event) {
                    is MapViewModel.MapEvent.NavigateBackFromMapWithResult -> {

                        setFragmentResult(
                            "map_result",
                            bundleOf("map_data" to event.result)
                        )

                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}