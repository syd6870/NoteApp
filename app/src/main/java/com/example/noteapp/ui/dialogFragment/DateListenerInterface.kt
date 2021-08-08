package com.example.noteapp.ui.dialogFragment

import android.app.DatePickerDialog
import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker


interface DateListenerInterface : DatePickerDialog.OnDateSetListener,Parcelable{
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}