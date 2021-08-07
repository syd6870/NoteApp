package com.example.noteapp.ui.viewNote

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentViewNoteBinding
import com.example.noteapp.ui.note.NoteViewModel
import com.example.noteapp.ui.note.NotesFragmentDirections
import com.example.noteapp.util.toDate
import com.example.noteapp.util.toTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ViewNoteFragment : Fragment(R.layout.fragment_view_note) {
    private val viewModel: ViewNoteViewModel by viewModels()
    private val TAG = "ViewNoteFragment"
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentViewNoteBinding.bind(view)
        binding.apply {
            Log.d(TAG, "Note "+viewModel.note)
            textviewViewNoteTitle.text=viewModel.noteTitle
            textviewViewNoteContent.text=viewModel.noteContent
            textviewViewNoteCreated.text="${viewModel.noteTime} \t ${viewModel.noteDate}"
            textviewViewNoteModified.text="${viewModel.noteLastEdit.toTime()} \t ${viewModel.noteLastEdit.toDate()}"
            textviewViewNoteLocation.text=viewModel.noteLocation
            textviewViewNoteTrack.text=viewModel.noteTracked.toString()
            textviewViewNoteRemindOn.text="${viewModel.noteRemindLong.toTime()} \t ${viewModel.noteRemindLong.toDate()}"
            fabEditNote.setOnClickListener{
                viewModel.onEditNoteClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notesEvent.collect { event->
                when(event){
                    is ViewNoteViewModel.NotesEvent.NavigateToEditNoteScreen -> {
                        val action=ViewNoteFragmentDirections.actionViewNoteFragmentToAddNewNote(event.note,"Edit Note")
                        findNavController().navigate(action)

                    }
                }
            }
        }
    }
}