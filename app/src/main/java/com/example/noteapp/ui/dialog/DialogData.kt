package com.example.noteapp.ui.dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DialogData(
    val title: String,
    val message: String,
    val positiveButtonText: String ="Yes",
    val negativeButtonText: String ="Cancel",
    val listener: MyDialogListener
) : Parcelable