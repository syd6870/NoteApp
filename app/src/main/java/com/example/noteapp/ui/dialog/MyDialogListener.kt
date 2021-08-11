package com.example.noteapp.ui.dialog

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


interface MyDialogListener : Parcelable {
    fun onButtonClick(positiveButtonClick:Boolean)

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}