package com.example.noteapp.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDialog : DialogFragment() {
    private val viewModel:ShowDialogViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(viewModel.dialogData.title)
            .setMessage(viewModel.dialogData.message)
            .setNegativeButton(viewModel.dialogData.negativeButtonText, null)
            .setPositiveButton(viewModel.dialogData.positiveButtonText) { _, _ ->

                viewModel.dialogData.listener.onButtonClick(true)

            }
            .create()

}