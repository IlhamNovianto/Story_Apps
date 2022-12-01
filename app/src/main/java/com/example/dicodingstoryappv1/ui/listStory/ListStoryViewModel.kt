package com.example.dicodingstoryappv1.ui.listStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.StoryRepository
import kotlinx.coroutines.launch


class ListStoryViewModel(
    private val userPreference: UserPreference,
    private val storyRepository: StoryRepository
    ): ViewModel() {
    fun getToken() : LiveData<String> {
        return  userPreference.getToken().asLiveData()
    }

    fun isLogin(): LiveData<Boolean> {
        return userPreference.isLogin().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            userPreference.logout()
        }
    }

    fun getStory(token: String)
    = storyRepository.getStories(token)
}