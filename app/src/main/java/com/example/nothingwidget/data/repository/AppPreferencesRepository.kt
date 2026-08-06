package com.example.nothingwidget.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferencesRepository(private val context: Context) {

    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Default to true (Dark Mode) as requested
            preferences[IS_DARK_THEME] ?: true
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }
}
