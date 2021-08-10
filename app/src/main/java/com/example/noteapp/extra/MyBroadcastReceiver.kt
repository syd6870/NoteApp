package com.example.noteapp.extra

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.hilt.Assisted
import javax.inject.Inject

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationBuilder=MyNotificationBuilder(context!!)
        notificationBuilder.createNotification()
    }
}