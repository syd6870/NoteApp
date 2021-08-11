package com.example.noteapp.ui.nearbyLocation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentNearbyLocationBinding
import com.example.noteapp.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class NearbyLocationFragment : Fragment(R.layout.fragment_nearby_location),
    NearbyLocationAdapter.onItemClickListener {
    private val viewModel: NearbyLocationViewModel by viewModels()
    private val TAG = "NearbyLocationFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    permissionGranted(view)
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    Log.d(TAG, "onViewCreated: No Permission")
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }


        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d(TAG, "onViewCreated: Self Permission true")
                permissionGranted(view)
            }

            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                Log.d(TAG, "onViewCreated: Self Permission true")
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.locationEvent.collect { event ->
                when (event) {
                    is NearbyLocationViewModel.LocationEvents.ShowToastMessage -> {
                        Toast.makeText(requireContext(), event.msg, Toast.LENGTH_LONG).show()
                    }
                    is NearbyLocationViewModel.LocationEvents.NavigateToShowDialog -> {
                        val action=NearbyLocationFragmentDirections.actionGlobalShowDialog(event.dialogData)
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }



        setHasOptionsMenu(true)

    }

    private fun permissionGranted(view: View) {
        val adapter = NearbyLocationAdapter(this)
        val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val binding = FragmentNearbyLocationBinding.bind(view)
        binding.apply {
            recyclerViewLocationNearby.adapter = adapter
            recyclerViewLocationNearby.layoutManager = layout
            recyclerViewLocationNearby.setHasFixedSize(true)


        }

        viewModel.noteListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.startLocationUpdates(activity as Activity, requireContext())

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_nearby, menu)

        val refresh = menu.findItem(R.id.action_refresh)
        val stopLocation = menu.findItem(R.id.action_stop_location_update)
        refresh.setOnMenuItemClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.nearbyLocationFragment)
            true
        }
        stopLocation.setOnMenuItemClickListener {
            viewModel.stopLocationUpdates()
            true
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopLocationUpdates()
    }

    //complete button Click
    override fun onItemClick(note: Note) {
        viewModel.onMarkCompletedClick(note)
    }
}