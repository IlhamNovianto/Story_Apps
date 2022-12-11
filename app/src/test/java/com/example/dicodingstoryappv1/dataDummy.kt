package com.example.dicodingstoryappv1

import com.example.dicodingstoryappv1.api.entity.StoryEntity
import com.example.dicodingstoryappv1.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


object DataDummy {

//Maps Dummy
    fun generateDummyMapsMarker(): GetAllStoryResponse {
        val stories = arrayListOf<ListStoryItem>()
        for (i in 0 until 10) {
            val story = ListStoryItem(
                "story-FvU4u0Vp2S3PMsFg",
                "Dimas",
                "2022-01-08T06:34:18.598Z",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "Lorem Ipsum",
                -16.002,
                -10.212
            )
            stories.add(story)
        }
        return GetAllStoryResponse(stories,false, "Error")
    }

//login dummy
    fun generateDummyLoginResponseSuccess(): LoginResponse {
        return LoginResponse(
            loginResult = generateDummyLoginResult(),
            error = false,
            message = "Success"
        )
    }
    fun  generateDummyLoginResult() : LoginResult {
        return LoginResult(
            "dico",
            "dico123@gmail",
            "lkshalkjdhfdf"
        )
    }

//register dummy
    fun generateDummyRegisterReponse(): RegisterResponse {
        return RegisterResponse(
            false,
            "success"
        )
    }


//Upload fun Dummy
    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }
    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }
    fun generateDummyStoryyResponseSuccess(): AddNewStoryResponse {
        return AddNewStoryResponse(
            false,
            "success"
        )
    }

//List Story
    fun generateDummyStoryListResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..10) {
            val story = StoryEntity(
                description = "desc $i",
                id = "id $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "name $i",
                photoUrl = "url/$i.jpg",
                createdAt = "17/03/00"
            )
            items.add(story)
        }
        return items
    }


}