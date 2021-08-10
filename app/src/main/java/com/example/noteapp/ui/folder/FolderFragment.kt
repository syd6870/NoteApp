package com.example.noteapp.ui.folder

import android.app.AlarmManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentFolderBinding
import com.example.noteapp.extra.MyAlarmManager
import com.example.noteapp.extra.MyNotificationBuilder
import com.example.noteapp.util.exhaustive
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@AndroidEntryPoint
class FolderFragment : Fragment(R.layout.fragment_folder), FolderAdapter.onItemClickListener {
    private val viewModel: FolderFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFolderBinding.bind(view)
        val folderAdapter = FolderAdapter(this)
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.alignItems= AlignItems.CENTER
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND



        binding.apply {
            recyclerViewFolders.apply {
                adapter = folderAdapter

                layoutManager = flexboxLayoutManager
                setHasFixedSize(true)
            }

            buttonAddFolders.setOnClickListener {
                //MyNotificationBuilder(requireContext()).createNotification()
                //delete me and the this button
                viewModel.setAlarm()

            }

        }
// TODO: 07-08-2021 Change tint of icon when theme changes
        val listOfFolders= listOf(
            Pair(R.drawable.ic_note_vector_final,Folder.NOTE.value),
            Pair(R.drawable.ic_baseline_checklist_24,Folder.TO_DO_LIST.value),
            Pair(R.drawable.ic_baseline_edit_24,Folder.JOURNAL.value),
            Pair(R.drawable.ic_note_vector_final,Folder.PROJECT.value),
            Pair(R.drawable.ic_book,Folder.READING.value),
            Pair(R.drawable.ic_baseline_event_24,Folder.EVENT.value)
        )

        folderAdapter.submitList(listOfFolders)


        viewLifecycleOwner.lifecycleScope.launchWhenStarted{
            viewModel.folderEvent.collect{event->
                when(event){
                    is FolderFragmentViewModel.FolderEvent.NavigateToNoteScreen -> {
                        val action=FolderFragmentDirections.actionStartFragmentToNotesFragment(event.folderName)
                        findNavController().navigate(action)
                    }
                }.exhaustive

            }
        }
    }



    override fun onItemClick(folderName: String) {
        viewModel.onFolderSelected(folderName)
    }
}

