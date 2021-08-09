package com.example.noteapp.extra

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import javax.inject.Inject

class MyAlarmManager @Inject constructor(val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    fun setAlarm(alarmId: Long, alarmTimeStamp: Long) {
        val id: Int = (alarmId % Int.MAX_VALUE).toInt()
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeStamp, createPendingIntent(intent, id))
    }

    private fun createPendingIntent(intent: Intent, id: Int): PendingIntent {
        return PendingIntent.getBroadcast(context.applicationContext, id, intent, 0)
    }

    fun cancelAlarm(alarmId: Long){
        val id: Int = (alarmId % Int.MAX_VALUE).toInt()
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        alarmManager.cancel(createPendingIntent(intent,id))
    }



}

fun main() {
    for (i in 0..100)
        println(System.currentTimeMillis() % Int.MAX_VALUE)

}
