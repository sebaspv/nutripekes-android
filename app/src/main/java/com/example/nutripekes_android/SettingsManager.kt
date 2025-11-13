package com.example.nutripekes_android

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val BIRTH_YEAR_KEY = intPreferencesKey("birth_year")
    }

    suspend fun saveBirthYear(year: Int) {
        dataStore.edit { settings ->
            settings[BIRTH_YEAR_KEY] = year
        }
    }

    val birthYearFlow: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[BIRTH_YEAR_KEY] ?: 0
    }
}