package com.example.noteapp.ui.note

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNotesBinding
import com.example.noteapp.util.onQueryTextChanged
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {
    private val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNotesBinding.bind(view)
        val noteAdapter = NoteAdapter()
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND



        binding.apply {
            recyclerViewNotes.apply {
                adapter = noteAdapter

                layoutManager = flexboxLayoutManager
                setHasFixedSize(true)
            }
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            noteAdapter.submitList(it)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_note, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                true
            }
            R.id.action_sort_by_date_created -> {
                true
            }
            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_hide_mute -> {
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_hide_untrack -> {
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_delete_completed_task -> {
                true
            }
            R.id.action_select_light_mode -> {
                true
            }
            R.id.action_select_dark_mode -> {
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

}