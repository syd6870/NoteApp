package com.example.noteapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table WHERE title LIKE '%'||:searchQuery || '%' ORDER BY created DESC")
    fun getNotes(searchQuery:String):Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE folder LIKE '%'||:folderName || '%' ORDER BY created DESC")
    fun getNotesByFolder(folderName:String):Flow<List<Note>>

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)



}