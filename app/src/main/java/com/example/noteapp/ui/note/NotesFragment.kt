package com.example.noteapp.ui.note

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.noteapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment :Fragment(R.layout.fragment_notes) {
    private val viewModel:ViewModel by viewModels()
}