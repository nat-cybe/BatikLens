package com.capstone.batiklens.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.capstone.batiklens.data.AuthRepository
import com.capstone.batiklens.utils.UserPreferences

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): AuthRepository{
        val preferences= UserPreferences.getInstance(dataStore)

        return AuthRepository.getInstance(preferences)
    }

}