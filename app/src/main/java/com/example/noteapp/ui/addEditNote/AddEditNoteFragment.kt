package com.example.noteapp.ui.addEditNote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentAddEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {
private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding= FragmentAddEditNoteBinding.bind(view)
        binding.apply {
            tilEditNoteTitle.editText?.setText(viewModel.noteTitle)
            tilEditNoteContent.editText?.setText(viewModel.noteContent)
            textViewEditNoteTime.text = viewModel.noteTime
            textViewEditNoteDate.text = viewModel.noteDate
            textViewEditNoteLocation.text = viewModel.noteLocation
            checkBoxIsTracked.isChecked=viewModel.noteTracked
            checkBoxIsTracked.jumpDrawablesToCurrentState() //avoid Animation
        }
    }
}