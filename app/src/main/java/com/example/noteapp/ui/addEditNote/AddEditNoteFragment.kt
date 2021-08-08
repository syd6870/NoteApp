package com.example.noteapp.ui.addEditNote

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentAddEditNoteBinding
import com.example.noteapp.ui.map.MapData
import com.example.noteapp.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val viewModel: AddEditNoteViewModel by viewModels()
    private val TAG = "AddEditNoteFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()
        val binding = FragmentAddEditNoteBinding.bind(view)
        binding.apply {
            tilEditNoteTitle.editText?.setText(viewModel.noteTitle)
            tilEditNoteContent.editText?.setText(viewModel.noteContent)
            viewModel.mutableTime.observe(viewLifecycleOwner) {
                textViewEditNoteTime.text = it
            }
            viewModel.mutableDate.observe(viewLifecycleOwner) {
                textViewEditNoteDate.text = it
            }
            viewModel.mutableAddress.observe(viewLifecycleOwner) {
                textViewEditNoteLocation.text = it
            }
            checkBoxIsTracked.isChecked = viewModel.noteTracked
            checkBoxIsTracked.jumpDrawablesToCurrentState() //avoid Animation

            buttonEditNotePickDate.setOnClickListener {
                //viewModel.onPickDateClick()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    requireContext(),
                    this@AddEditNoteFragment,
                    year,
                    month,
                    day
                ).show()
            }
            buttonEditNotePickTime.setOnClickListener {
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                TimePickerDialog(context, this@AddEditNoteFragment, hour, minute, false).show()
            }
            buttonEditNotePickLocation.setOnClickListener {
                viewModel.onChooseLocationClick()
                // TODO: 06-08-2021
            }

            tilEditNoteTitle.editText!!.addTextChangedListener {
                viewModel.noteTitle = it.toString()
            }

            tilEditNoteContent.editText!!.addTextChangedListener {
                viewModel.noteContent = it.toString()
            }

            checkBoxIsTracked.setOnCheckedChangeListener { _, isChecked ->
                viewModel.noteTracked = isChecked
            }

            buttonSaveNote.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        setFragmentResultListener("map_result") { _, bundle ->
            val result = bundle.getParcelable<MapData>("map_data")
            Log.d(TAG, "onFragResult: $result")
            viewModel.noteAddress = result?.address ?: "Earth"
            viewModel.noteLocationLatitude = result?.latitude?.toFloat() ?: 0.0F
            viewModel.noteLocationLongitude = result?.longitude?.toFloat() ?: 0.0F


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditNoteEvent.collect { event ->
                when (event) {
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateBackWithResult -> {
                        binding.tilEditNoteTitle.clearFocus()
                        binding.tilEditNoteContent.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        //findNavController().popBackStack()
                        val action =
                            AddEditNoteFragmentDirections.actionAddNewNoteToNotesFragment(event.folderName)
                        findNavController().navigate(action)


                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.showInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateToDatePicker -> {
                        val action =
                            AddEditNoteFragmentDirections.actionAddNewNoteToDatePickerFragment(event.listener)
                        findNavController().navigate(action)
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateToTimePicker -> {
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateToMapFragment -> {
                        val action = AddEditNoteFragmentDirections.actionAddNewNoteToMapFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onDateSet: $dayOfMonth/${month}/$year")
        viewModel.noteRemindDate = "$dayOfMonth/${month}/$year"
        Snackbar.make(requireView(), "$dayOfMonth/${month}/$year", Snackbar.LENGTH_LONG).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        val time24 = SimpleDateFormat("HH:mm", Locale("EN", "IN"))
        val time12 = SimpleDateFormat("hh:mm a", Locale("EN", "IN"))
        val date: Date = time24.parse("$hourOfDay:$minute") ?: Date()
        viewModel.noteRemindTime = time12.format(date)
    }
}