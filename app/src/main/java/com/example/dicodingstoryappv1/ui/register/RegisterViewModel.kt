package com.example.dicodingstoryappv1.ui.register

import androidx.lifecycle.ViewModel
import com.example.dicodingstoryappv1.Preference.UserPreference

class RegisterViewModel(private val preference: UserPreference) : ViewModel() {
    fun register(name: String, email:String, password:String) =
        preference.register(name, email, password)
}