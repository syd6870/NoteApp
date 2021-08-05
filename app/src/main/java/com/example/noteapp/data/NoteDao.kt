package com.example.noteapp.data

import androidx.room.*
import com.example.noteapp.ui.note.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    fun getNotes(searchQuery: String,sortOrder: SortOrder,hideCompleted: Boolean):Flow<List<Note>> =
        when(sortOrder)
        {
            SortOrder.BY_DATE->getNotesSortByDate(searchQuery, hideCompleted)
            SortOrder.BY_NAME->getNotesSortByName(searchQuery, hideCompleted)
        }


    @Query("SELECT * FROM note_table WHERE (isCompleted!=:hideCompleted OR isCompleted=0) AND title LIKE '%'||:searchQuery || '%' ORDER BY title DESC")
    fun getNotesSortByName(searchQuery:String,hideCompleted:Boolean):Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE (isCompleted!=:hideCompleted OR isCompleted=0) AND title LIKE '%'||:searchQuery || '%' ORDER BY created DESC")
    fun getNotesSortByDate(searchQuery:String,hideCompleted:Boolean):Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE folder LIKE '%'||:folderName || '%' ORDER BY created DESC")
    fun getNotesByFolder(folderName:String):Flow<List<Note>>

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)



}