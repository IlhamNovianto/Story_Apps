package com.example.dicodingstoryappv1.factoryViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.StoryRepository
import com.example.dicodingstoryappv1.injection.StoryInjection
import com.example.dicodingstoryappv1.injection.UserInjection
import com.example.dicodingstoryappv1.ui.OnCamera.AddStoryViewModel
import com.example.dicodingstoryappv1.ui.listStory.ListStoryViewModel
import com.example.dicodingstoryappv1.ui.maps.MapsViewModel

class StoryFactoryViewModel private constructor(
    private val userPreference: UserPreference,
    private val storyRepository: StoryRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(userPreference, storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository,userPreference) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository,userPreference) as T
            }
            else -> {
                throw  IllegalArgumentException("uknow viewmodel class:" + modelClass.name)
            }
        }
    }
    companion object {
        private var instance: StoryFactoryViewModel? = null
        fun getInstance(context: Context): StoryFactoryViewModel =
            instance ?: synchronized(this) {
                instance ?: StoryFactoryViewModel(
                    UserInjection.providePreferences(context),
                    StoryInjection.provider(context)

                )
            }.also { instance = it }
    }
}