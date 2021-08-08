package com.example.noteapp.ui.note

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.data.SortOrder
import com.example.noteapp.databinding.FragmentNotesBinding
import com.example.noteapp.util.exhaustive
import com.example.noteapp.util.onQueryTextChanged
import com.google.android.flexbox.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes),NoteAdapter.onItemClickListener {
    private val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNotesBinding.bind(view)
        val noteAdapter = NoteAdapter(this)
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

            buttonAddNote.setOnClickListener{
                viewModel.onAddNewNoteClick()
            }
        }

        setFragmentResultListener("add_edit_request"){_,bundle ->
            val result=bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            noteAdapter.submitList(it)
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noteEvent.collect { event->
                when(event){
                    is NoteViewModel.NotesEvent.NavigateToAddNoteScreen -> {
                        val action=NotesFragmentDirections.actionNotesFragmentToAddNewNote(null,"New Note")
                        findNavController().navigate(action)
                    }

                    is NoteViewModel.NotesEvent.NavigateToViewNoteScreen -> {
                        val action=NotesFragmentDirections.actionNotesFragmentToViewNoteFragment(event.note)
                        findNavController().navigate(action)
                    }
                    is NoteViewModel.NotesEvent.ShowNoteSavedConfirmationMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                    is NoteViewModel.NotesEvent.NavigateToDeleteAllCompleteScreen -> {
                        val action=NotesFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                    is NoteViewModel.NotesEvent.RecreateActivity -> {
                        activity?.recreate()
                    }
                }.exhaustive
            }
        }


        setHasOptionsMenu(true)
    }


    override fun onItemClick(note: Note) {
        viewModel.onNoteSelected(note)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_note, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed).isChecked =
                viewModel.preferenceFlow.first().hideCompleted
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            /* R.id.action_hide_mute -> {
                 item.isChecked = !item.isChecked
                 true
             }
             R.id.action_hide_untrack -> {
                 item.isChecked = !item.isChecked
                 true
             }*/
            R.id.action_delete_completed_task -> {
                viewModel.onDeleteAllCompletedClick()
                true
            }
            R.id.action_select_light_mode -> {
                viewModel.onThemeSelected("light")
                Toast.makeText(requireContext(), "Theme:Light Mode!", Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            R.id.action_select_dark_mode -> {
                viewModel.onThemeSelected("dark")
                Toast.makeText(requireContext(), "Theme:Dark Mode!", Toast.LENGTH_SHORT)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

}