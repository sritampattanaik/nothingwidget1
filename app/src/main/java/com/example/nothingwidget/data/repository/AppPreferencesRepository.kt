package com.example.nothingwidget.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferencesRepository(private val context: Context) {

    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    private val IS_HAPTIC_FEEDBACK = booleanPreferencesKey("is_haptic_feedback")
    private val IS_24_HOUR_CLOCK = booleanPreferencesKey("is_24_hour_clock")
    private val IS_CELSIUS = booleanPreferencesKey("is_celsius")
    private val SELECTED_CITY = stringPreferencesKey("selected_city")

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_DARK_THEME] ?: true }
    val isHapticFeedbackFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_HAPTIC_FEEDBACK] ?: true }
    val is24HourClockFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_24_HOUR_CLOCK] ?: true }
    val isCelsiusFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_CELSIUS] ?: true }
    val selectedCityFlow: Flow<String> = context.dataStore.data.map { it[SELECTED_CITY] ?: "London" }

    suspend fun setDarkTheme(enabled: Boolean) = context.dataStore.edit { it[IS_DARK_THEME] = enabled }
    suspend fun setHapticFeedback(enabled: Boolean) = context.dataStore.edit { it[IS_HAPTIC_FEEDBACK] = enabled }
    suspend fun set24HourClock(enabled: Boolean) = context.dataStore.edit { it[IS_24_HOUR_CLOCK] = enabled }
    suspend fun setCelsius(enabled: Boolean) = context.dataStore.edit { it[IS_CELSIUS] = enabled }
    suspend fun setSelectedCity(city: String) = context.dataStore.edit { it[SELECTED_CITY] = city }
}
