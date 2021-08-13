package com.example.noteapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.noteapp.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "PreferencesManager"

enum class SortOrder { BY_NAME, BY_DATE }
enum class Theme { LIGHT_THEME, DARK_THEME }

data class FilterPreference(
    val sortOrder: SortOrder,
    val hideCompleted: Boolean,
    val theme: String
)


@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {
    val Context.datastore: DataStore<Preferences> by preferencesDataStore("user_preferences")


    val preferenceFlow = context.datastore.data
        .catch { exception->
            if (exception is IOException){
                Log.e(TAG, "Error reading pref",exception )
                emit(emptyPreferences())
            }
            else{
                throw exception
            }

        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferenceKeys.SORT_BY] ?: SortOrder.BY_NAME.name
            )

            val hideCompleted = preferences[PreferenceKeys.HIDE_COMPLETED] ?: false
            val theme = preferences[PreferenceKeys.THEME] ?: "light"
            FilterPreference(sortOrder,hideCompleted, theme)
        }



    suspend fun updateSortOrder(sortOrder: SortOrder){
        context.datastore.edit{ preference->
            preference[PreferenceKeys.SORT_BY] = sortOrder.name

        }
    }
    suspend fun updateHideCompleted(hideCompleted: Boolean){
        context.datastore.edit{ preference->
            preference[PreferenceKeys.HIDE_COMPLETED] = hideCompleted

        }
    }
    suspend fun updateTheme(theme: String){
        context.datastore.edit{ preference->
            preference[PreferenceKeys.THEME] = theme

        }
    }





    private object PreferenceKeys {
        val SORT_BY = stringPreferencesKey("sort_by")
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
        val THEME = stringPreferencesKey("theme")
    }
}