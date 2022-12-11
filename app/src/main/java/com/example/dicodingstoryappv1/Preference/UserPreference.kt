package com.example.dicodingstoryappv1.Preference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.dicodingstoryappv1.api.ApiService
import com.example.dicodingstoryappv1.api.response.LoginResponse
import com.example.dicodingstoryappv1.api.response.RegisterResponse
import com.example.dicodingstoryappv1.api.response.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference (
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
    ){
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            if (result.error) {
                emit(Result.Error(result.message))
            } else {
                emit(Result.Success(result))
            }
        } catch (e:Exception) {
            Log.d("UserRepository", "Login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(
    name: String,
    email: String,
    password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.register(name, email, password)
            if (result.error){
                emit(Result.Error(result.message))
            }else {
                emit(Result.Success(result))
            }
        } catch (e:Exception) {
            Log.d("UserRepository", "Register: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    fun isLogin(): Flow<Boolean> {
        return  dataStore.data.map { preferences ->
            preferences[STATE] ?: false
        }
    }

     suspend fun setState(token: String, isLogin: Boolean) { dataStore.edit {
            preferences ->
             preferences[TOKEN] = token
             preferences[STATE] = isLogin
     }
    }
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val TOKEN = stringPreferencesKey("token")
        private val STATE = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}