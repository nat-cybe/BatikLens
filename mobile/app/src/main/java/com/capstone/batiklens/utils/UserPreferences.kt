package com.capstone.batiklens.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){
    private val THEMEKEY = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean>{
        return dataStore.data.map {preferences ->
            preferences[THEMEKEY] ?: false
        }
    }

    suspend fun saveThemeSettings(isDarkModeActive: Boolean){
        dataStore.edit {preferences ->
            preferences[THEMEKEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}