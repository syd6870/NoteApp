package com.example.noteapp.di

import android.app.Application
import androidx.room.Room
import com.example.noteapp.data.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: NoteDatabase.Callback
    ) = Room.databaseBuilder(app, NoteDatabase::class.java, "note_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()


    @Provides
    fun provideNoteDao(db:NoteDatabase)=db.noteDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope()= CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope