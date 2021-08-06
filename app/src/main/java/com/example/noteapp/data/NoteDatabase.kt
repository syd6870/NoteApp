package com.example.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao


    class Callback @Inject constructor(
        private val database: Provider<NoteDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //this method will execute when application is opened first time
            //lazy init database with Provider<>
            val dao = database.get().noteDao()

            applicationScope.launch {
                dao.insert(Note("Dummy Title"))
                dao.insert(Note("Dummy Title2",isCompleted = true))
                dao.insert(Note("Dummy Title4",isTracked = true))
                dao.insert(Note("Dummy Title5"))
                dao.insert(Note("Dummy Title6"))
                dao.insert(Note("Dummy Title7"))
            }

        }
    }

}