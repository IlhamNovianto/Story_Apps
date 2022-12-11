package com.example.dicodingstoryappv1.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstoryappv1.Preference.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel (private val userPreference: UserPreference): ViewModel(){

    fun setToken(token: String, state: Boolean) {
        viewModelScope.launch {
            userPreference.setState(token, state)
        }
    }
    fun getToken(): LiveData<String> {
        return userPreference.getToken().asLiveData()
    }
    fun login(email: String, password: String) = userPreference.login(email, password)
}