package com.example.dicodingstoryappv1.factoryViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.injection.UserInjection
import com.example.dicodingstoryappv1.ui.login.LoginViewModel
import com.example.dicodingstoryappv1.ui.register.RegisterViewModel

class UserFactoryViewModel private constructor(private val pref: UserPreference):
     ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(pref) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: UserFactoryViewModel? = null
        fun getInstance(context: Context): UserFactoryViewModel =
            instance ?: synchronized(this) {
                instance ?: UserFactoryViewModel(UserInjection.providePreferences(context))
            }.also { instance = it }
    }
     }