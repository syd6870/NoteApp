package com.example.noteapp.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


const val ADD_NOTE_RESULT_OK= Activity.RESULT_FIRST_USER
const val EDIT_NOTE_RESULT_OK= Activity.RESULT_FIRST_USER+1