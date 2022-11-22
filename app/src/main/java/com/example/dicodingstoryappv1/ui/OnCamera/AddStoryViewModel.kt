package com.example.dicodingstoryappv1.ui.OnCamera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private var storyRepository: StoryRepository,
    private val userPreference: UserPreference

) : ViewModel() {
    fun getToken(): LiveData<String> {
        return userPreference.getToken().asLiveData()
    }
    fun addStory(
        token: String,
        photo: MultipartBody.Part,
        desc: RequestBody,
        lat: String,
        lon: String) =
        storyRepository.addStories(
            token, photo, desc, lat, lon)
}