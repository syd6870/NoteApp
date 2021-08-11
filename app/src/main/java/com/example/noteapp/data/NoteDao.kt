package com.example.noteapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NoteDao {

    fun getNotes(
        searchQuery: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean,
        folderName: String
    ): Flow<List<Note>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getNotesSortByDate(searchQuery, hideCompleted, folderName)
            SortOrder.BY_NAME -> getNotesSortByName(searchQuery, hideCompleted, folderName)
        }


    @Query("SELECT * FROM note_table WHERE folder LIKE '%'||:folderName || '%' AND (isCompleted!=:hideCompleted OR isCompleted=0) AND title LIKE '%'||:searchQuery || '%' ORDER BY title DESC")
    fun getNotesSortByName(
        searchQuery: String,
        hideCompleted: Boolean,
        folderName: String
    ): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE folder LIKE '%'||:folderName || '%'AND (isCompleted!=:hideCompleted OR isCompleted=0) AND title LIKE '%'||:searchQuery || '%' ORDER BY created DESC")
    fun getNotesSortByDate(
        searchQuery: String,
        hideCompleted: Boolean,
        folderName: String
    ): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE folder LIKE '%'||:folderName || '%' ORDER BY created DESC")
    fun getNotesByFolder(folderName: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table WHERE isCompleted=1")
    suspend fun deleteCompletedNotes()

    @Query("SELECT * FROM note_table WHERE (isCompleted=0) AND (isTracked=1)")
    fun getAllTrackedNonCompletedNote(): Flow<List<Note>>

    @Query("SELECT * FROM note_table")
    fun test(): Flow<List<Note>>

}