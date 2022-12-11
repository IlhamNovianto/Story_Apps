package com.example.dicodingstoryappv1.api.response

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.dicodingstoryappv1.api.ApiService
import com.example.dicodingstoryappv1.api.entity.StoryEntity
import com.example.dicodingstoryappv1.database.StoryDatabase
import com.example.dicodingstoryappv1.database.StoryRemoteMediator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStories(token: String): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.databaseDao().getAllstories()
            }
        ).liveData
    }

    fun addStories(
        token: String,
        photo: MultipartBody.Part,
        desc: RequestBody,
        lat: String? = null,
        lon: String? = null
    ): LiveData<Result<AddNewStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val latitude = lat?.toRequestBody("text/plain".toMediaType())
            val longitude = lon?.toRequestBody("text/plain".toMediaType())
            val result =
                apiService.postStory(
                    "Bearer $token",
                    photo,
                    desc,
                    latitude,
                    longitude)
            if (result.error) {
                emit(Result.Error(result.message))
            } else {
                emit(Result.Success(result))
            }
        } catch (e:Exception) {
            Log.d("StoryRepository", "addStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryMaps(token: String, location: Int):
            LiveData<Result<GetAllStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getMarker("Bearer $token", location)
            if (result.error) {
                emit(Result.Error(result.message))
            } else {
            emit(Result.Success(result))
            }
        } catch (e:Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(storyDatabase, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}