package com.example.dicodingstoryappv1.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.StoryRepository


class MapsViewModel(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
    ): ViewModel() {

    fun getStoryMaps(
        token: String,
        isLocation: Int
    ) = storyRepository.getStoryMaps(token, isLocation)

    fun getToken(): LiveData<String> {
    return userPreference.getToken().asLiveData()
}
}