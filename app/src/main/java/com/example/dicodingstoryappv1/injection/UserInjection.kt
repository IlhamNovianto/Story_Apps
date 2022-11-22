package com.example.dicodingstoryappv1.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object UserInjection {
    fun providePreferences(context: Context): UserPreference{
        val apiServ = ApiConfig.getApiService()
        return UserPreference.getInstance(context.dataStore, apiServ)
    }
}