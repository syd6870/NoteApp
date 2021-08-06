package com.example.noteapp.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.noteapp.R
import com.example.noteapp.data.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val preferencesManager=PreferencesManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(getTheme())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.findNavController()

        setupActionBarWithNavController(navController)

    }




    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}


const val ADD_NOTE_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_NOTE_RESULT_OK = Activity.RESULT_FIRST_USER + 1