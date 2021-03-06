package com.example.noteapp.ui.addEditNote

import android.util.Log
import android.widget.DatePicker
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDao
import com.example.noteapp.extra.MyAlarmManager
import com.example.noteapp.ui.ADD_NOTE_RESULT_OK
import com.example.noteapp.ui.EDIT_NOTE_RESULT_OK
import com.example.noteapp.ui.dialogFragment.DateListenerInterface
import com.example.noteapp.ui.folder.Folder
import com.example.noteapp.util.toDate
import com.example.noteapp.util.toTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    @Assisted private val state: SavedStateHandle,
    private val myAlarmManager: MyAlarmManager
) : ViewModel() {

    private val TAG = "AddEditTaskViewModel"
    private val currentDatePlus1 = System.currentTimeMillis() + 86400000L;
    private val currentTime = System.currentTimeMillis();
    val note = state.get<Note>("note")


    var noteTitle = state.get<String>("noteTitle") ?: note?.title ?: ""
        set(value) {
            field = value
            state.set("noteTitle", value)
        }

    var noteContent = state.get<String>("noteContent") ?: note?.content ?: ""
        set(value) {
            field = value
            state.set("noteContent", value)
        }

    private var noteRemindLong =
        state.get<Long>("noteReminderLong") ?: note?.remindOn ?: currentDatePlus1
        set(value) {
            field = value
            state.set("noteReminderLong", value)
        }

    var noteRemindTime =
        state.get<String>("noteReminderTime") ?: note?.reminderTime ?: currentDatePlus1.toTime()
        set(value) {
            field = value
            state.set("noteReminderTime", value)
            mutableTime.value = value
        }

    var noteRemindDate =
        state.get<String>("noteReminderDate") ?: note?.reminderDate ?: currentDatePlus1.toDate()
        set(value) {
            field = value
            state.set("noteReminderDate", value)
            mutableDate.value = value
        }

    var noteLocationLatitude =
        state.get<Float>("noteLocationLatitude") ?: note?.latitude ?: 0.0f
        set(value) {
            field = value
            state.set("noteLocationLatitude", value)
        }

    var noteLocationLongitude =
        state.get<Float>("noteLocationLongitude") ?: note?.longitude ?: 0.0f
        set(value) {
            field = value
            state.set("noteLocationLongitude", value)
        }

    var noteAddress = state.get<String>("noteAddress") ?: note?.address ?: ""
        set(value) {
            field = value
            state.set("noteAddress", value)
            mutableAddress.value = value
        }

    var noteTracked = state.get<Boolean>("noteTracked") ?: note?.isTracked ?: false
        set(value) {
            field = value
            state.set("noteTracked", value)
        }

    var noteFolder = state.get<String>("noteFolder") ?: note?.folder ?: Folder.NOTE.value
        set(value) {
            field = value
            state.set("noteFolder", value)
        }


    val mutableDate = MutableLiveData(noteRemindDate)
    val mutableTime = MutableLiveData(noteRemindTime)
    val mutableAddress = MutableLiveData(noteAddress)

    private val addEditNoteEventChannel = Channel<AddEditNoteEvent>()
    val addEditNoteEvent = addEditNoteEventChannel.receiveAsFlow()

    fun onSaveClick() {

        val sdf = SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale("EN", "IN"))
        val date: Date = sdf.parse("${mutableTime.value} ${mutableDate.value}") ?: Date()
        noteRemindLong = date.time

        if (noteTitle.isBlank()) {
            showInvalidInputMessage("Title cannot be Empty")
            return
        }
        if (noteContent.isBlank()) {
            showInvalidInputMessage("Note body cannot be Empty")
            return
        }
        if (noteRemindLong < currentTime) {
            showInvalidInputMessage("Time already Passed")
            return
        }
        if(noteAddress.isBlank()){
            showInvalidInputMessage("Location Empty")
            return
        }

        Log.d(TAG, "onSaveClick: $noteRemindLong")

        if (note != null) {
            val updatedNote = note.copy(
                title = noteTitle,
                content = noteContent,
                remindOn = noteRemindLong,
                latitude = noteLocationLatitude,
                longitude = noteLocationLongitude,
                address = noteAddress,
                lastEditOn = currentTime,
                isTracked = noteTracked,
                folder = noteFolder
            )

            updateNote(updatedNote)
            setAlarm(updatedNote)
        } else {
            val note = Note(title = noteTitle,
                content = noteContent,
                remindOn = noteRemindLong,
                latitude = noteLocationLatitude,
                longitude = noteLocationLongitude,
                address = noteAddress,
                isTracked = noteTracked,)
            createNote(note)
            setAlarm(note)
        }

    }

    private fun setAlarm(note:Note) {
        if (noteTracked) {
            myAlarmManager.setAlarm(note.created, note.remindOn)
            Log.d(TAG, "setAlarm: Alarm Set. NoteTracked= ${noteTracked}")
        } else {
            myAlarmManager.cancelAlarm(note.created)
            Log.d(TAG, "setAlarm: Alarm Cancelled. NoteTracked= ${noteTracked}")
        }
    }

    private fun createNote(note: Note) = viewModelScope.launch {
        noteDao.insert(note)
        addEditNoteEventChannel.send(
            AddEditNoteEvent.NavigateBackWithResult(
                ADD_NOTE_RESULT_OK,
                noteFolder
            )
        )
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        noteDao.update(note)
        addEditNoteEventChannel.send(
            AddEditNoteEvent.NavigateBackWithResult(
                EDIT_NOTE_RESULT_OK,
                noteFolder
            )
        )
        Log.d(TAG, "updateNote: ${note.remindOn}")
    }

    private fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditNoteEventChannel.send(AddEditNoteEvent.showInvalidInputMessage(message))
    }

    /*fun onPickDateClick() = viewModelScope.launch {
        val listener = object : DateListenerInterface {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                noteRemindDate = "$dayOfMonth/${month}/$year"
                Log.d(TAG, "onDateSet: $dayOfMonth/${month}/$year")
            }
        }

        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateToDatePicker(listener))
    }*/

    fun onChooseLocationClick() = test()

    private fun test() = viewModelScope.launch {
        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateToMapFragment)
    }

    sealed class AddEditNoteEvent {
        data class showInvalidInputMessage(val msg: String) : AddEditNoteEvent()
        data class NavigateBackWithResult(val result: Int, val folderName: String) :
            AddEditNoteEvent()

        object NavigateToMapFragment : AddEditNoteEvent()
        data class NavigateToDatePicker(val listener: DateListenerInterface) : AddEditNoteEvent()
        data class NavigateToTimePicker(val listener: DateListenerInterface) : AddEditNoteEvent()
    }


}