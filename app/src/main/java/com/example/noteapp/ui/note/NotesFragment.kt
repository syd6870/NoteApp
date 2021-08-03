package com.example.noteapp.ui.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNotesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {
    private val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNotesBinding.bind(view)
        val noteAdapter = NoteAdapter()

        binding.apply {
            recyclerViewNotes.apply {
                adapter = noteAdapter
                layoutManager = GridLayoutManager(requireContext(),2)
                setHasFixedSize(true)
            }
        }

        viewModel.notes.observe(viewLifecycleOwner){
            noteAdapter.submitList(it)
        }
    }

}