package com.example.noteapp.ui.addEditNote

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentAddEditNoteBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {
    private val viewModel: AddEditNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditNoteBinding.bind(view)
        binding.apply {
            tilEditNoteTitle.editText?.setText(viewModel.noteTitle)
            tilEditNoteContent.editText?.setText(viewModel.noteContent)
            textViewEditNoteTime.text = viewModel.noteRemindTime
            textViewEditNoteDate.text = viewModel.noteRemindDate
            textViewEditNoteLocation.text = viewModel.noteAddress
            checkBoxIsTracked.isChecked = viewModel.noteTracked
            checkBoxIsTracked.jumpDrawablesToCurrentState() //avoid Animation

            buttonEditNotePickDate.setOnClickListener {
                // TODO: 06-08-2021
            }
            buttonEditNotePickTime.setOnClickListener {
                // TODO: 06-08-2021
            }
            buttonEditNotePickLocation.setOnClickListener {
                // TODO: 06-08-2021
            }

            tilEditNoteTitle.editText!!.addTextChangedListener {
                viewModel.noteTitle = it.toString()
            }

            tilEditNoteContent.editText!!.addTextChangedListener {
                viewModel.noteContent = it.toString()
            }

            checkBoxIsTracked.setOnCheckedChangeListener{_,isChecked->
                viewModel.noteTracked=isChecked
            }

            buttonSaveNote.setOnClickListener {
                viewModel.onSaveClick()
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditNoteEvent.collect{event->
                when (event) {
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateBackWithResult -> {
                        binding.tilEditNoteTitle.clearFocus()
                        binding.tilEditNoteContent.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        //findNavController().popBackStack()
                        val action=AddEditNoteFragmentDirections.actionAddNewNoteToNotesFragment(event.folderName)
                        findNavController().navigate(action)


                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.showInvalidInputMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}