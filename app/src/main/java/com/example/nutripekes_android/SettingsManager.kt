package com.example.nutripekes_android

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

private val Context.dataStore by preferencesDataStore(name = "settings")

data class DailyProgress(
    val verduras: Int = 0,
    val animal: Int = 0,
    val leguminosas: Int = 0,
    val cereales: Int = 0,
    val agua: Int = 0
)

class SettingsManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val BIRTH_YEAR_KEY = intPreferencesKey("birth_year")

        val VERDURAS_KEY = intPreferencesKey("count_verduras")
        val ANIMAL_KEY = intPreferencesKey("count_animal")
        val LEGUMINOSAS_KEY = intPreferencesKey("count_leguminosas")
        val CEREALES_KEY = intPreferencesKey("count_cereales")
        val AGUA_KEY = intPreferencesKey("count_agua")
        val LAST_DATE_KEY = longPreferencesKey("last_saved_date")

        val TUTORIAL_SHOWN_KEY = booleanPreferencesKey("tutorial_shown")
    }

    suspend fun saveBirthYear(year: Int) {
        dataStore.edit { settings ->
            settings[BIRTH_YEAR_KEY] = year
        }
    }

    val birthYearFlow: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[BIRTH_YEAR_KEY] ?: 0
    }

    private fun getTodayStart(): Long{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    suspend fun savePortion(key: androidx.datastore.preferences.core.Preferences.Key<Int>, count: Int){
        val today = getTodayStart()
        dataStore.edit { settings ->
            settings[key] = count
            settings[LAST_DATE_KEY] = today

        }
    }

    val dailyProgressFlow: Flow<DailyProgress> = dataStore.data.map { preferences ->
        val savedDate = preferences[LAST_DATE_KEY] ?: 0L
        val today = getTodayStart()

        if(savedDate != today) {
            DailyProgress()
        }else{
            DailyProgress(
                verduras = preferences[VERDURAS_KEY] ?: 0,
                animal = preferences[ANIMAL_KEY] ?: 0,
                leguminosas = preferences[LEGUMINOSAS_KEY] ?: 0,
                cereales = preferences[CEREALES_KEY] ?: 0,
                agua = preferences[AGUA_KEY] ?: 0
            )
        }
    }

    suspend fun setTutorialShown(shown: Boolean) {
        dataStore.edit { it[TUTORIAL_SHOWN_KEY] = shown }
    }

    val tutorialShownFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TUTORIAL_SHOWN_KEY] ?: false
    }
}