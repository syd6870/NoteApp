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


@AndroidEntryPoint
class MapFragment() : Fragment(R.layout.fragment_map) {
    /*private val TAG = "MapFragment"
    private val viewModel: MapViewModel by viewModels()
    private val mapImage =
        MapImageFactory.fromResource(context?.resources, R.drawable.ic_baseline_location_on_24);
    private lateinit var mapMarker: MapMarker*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* viewModel.geocoder= Geocoder(context)
        val binding = FragmentMapBinding.bind(view)
        binding.apply {1
            mapView.onCreate(savedInstanceState)

            mapView.mapScene.loadScene(MapStyle.NORMAL_DAY) { errorCode ->
                if (errorCode == null) {
                    mapView.camera.target = GeoCoordinates(52.530932, 13.384915)
                    mapView.camera.zoomLevel = 14.0
                } else {
                    Log.d(TAG, "onLoadScene failed: $errorCode")
                }
            }





            mapView.mapScene.addMapMarker(mapMarker)
            mapView.gestures.setTapListener { touchPoint ->
                val geoCoordinates = mapView.camera.viewToGeoCoordinates(touchPoint);

                mapMarker = MapMarker(geoCoordinates);
                mapMarker.addImage(mapImage, MapMarkerImageStyle());
                Log.d(TAG, "Tap at: " + geoCoordinates);

                viewModel.getAddressFromCoordinates(geoCoordinates)
            }

            *//*viewModel.enableButton.observe(viewLifecycleOwner){
                buttonConfirmMapLocation.isEnabled=it
            }*//*


            buttonConfirmMapLocation.setOnClickListener {
                viewModel.onConfirmButtonClick()
            }
        }*/


        /*viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mapEvent.collect { event ->
                when (event) {
                    is MapViewModel.MapEvent.NavigateBackFromMapWithResult -> {

                        setFragmentResult(
                            "map_result",
                            bundleOf("map_data" to event.result)
                        )

                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }*/
    }
}