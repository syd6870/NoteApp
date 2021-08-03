package com.example.noteapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import java.text.DateFormat

@Entity(tableName = "note_table")
@Parcelize
data class Note(
    val title: String,
    val content: String="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla aliquet enim erat, et tempor neque porttitor vel. In cursus elit at luctus laoreet. Phasellus quis congue nulla. Integer odio odio, ultrices ut quam consectetur, vestibulum mattis augue. Praesent ornare nunc et ipsum posuere pulvinar.",
    val folder: String="Note",
    val lastEditOn: Long = System.currentTimeMillis(),
    val created: Long = System.currentTimeMillis(),
    val latitude: Float=0.0f,
    val longitude: Float=0.0f,
    val isMuted: Boolean = false,
    val isTracked: Boolean = true,
    val isCompleted: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val lastEditDateFormat: String
        get() = DateFormat.getDateTimeInstance().format(lastEditOn)


    val createdDate: String
        get() {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("EN", "IN"))
            return simpleDateFormat.format(created)
        }
    val createdTime: String
        get() {
            val simpleTimeFormat = SimpleDateFormat("hh:mm: a", Locale("EN", "IN"))
            return simpleTimeFormat.format(created)
        }

}