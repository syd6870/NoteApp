package com.example.noteapp.ui.deleteAllCompleted

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.noteapp.ui.note.NotesFragment
import com.example.noteapp.ui.viewNote.ViewNoteFragment
import com.example.noteapp.ui.viewNote.ViewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment() {
    private val viewModel: DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage(viewModel.message)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.onConfirmClick()
                setFragmentResult(
                    "item_deleted",
                    bundleOf("item_deleted_result" to viewModel.resultMessage)
                )

                findNavController().popBackStack()
                findNavController().popBackStack()

            }
            .create()



}