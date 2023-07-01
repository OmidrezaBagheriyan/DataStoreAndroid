package com.omidrezabagherian.datastoretutorials

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingDataStore(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SETTING_KEY")
    private val dataStore = context.dataStore

    companion object {
        val darkModeKey = stringPreferencesKey("DARKMODE_KEY")
        val langModeKey = stringPreferencesKey("LANGMODE_KEY")
    }

    suspend fun setTheme(darkMode: String) {
        dataStore.edit { preferences ->
            preferences[darkModeKey] = darkMode
        }
    }

    suspend fun setLang(langMode: String) {
        dataStore.edit { preferences ->
            preferences[langModeKey] = langMode
        }
    }

    fun getTheme(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[darkModeKey] ?: Theme.DEFAULT.name
            }
    }

    fun getLang(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[langModeKey] ?: Lang.DEFAULT.name
            }
    }
}