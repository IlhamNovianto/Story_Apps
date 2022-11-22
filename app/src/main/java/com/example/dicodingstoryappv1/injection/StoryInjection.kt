package com.example.dicodingstoryappv1.injection

import android.content.Context
import com.example.dicodingstoryappv1.api.ApiConfig
import com.example.dicodingstoryappv1.api.response.StoryRepository
import com.example.dicodingstoryappv1.database.StoryDatabase

object StoryInjection {
    fun provider(context: Context): StoryRepository {
        val apiServ = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(database, apiServ)
    }
}