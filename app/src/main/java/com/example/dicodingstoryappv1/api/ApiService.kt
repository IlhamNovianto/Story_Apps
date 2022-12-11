package com.example.dicodingstoryappv1.api

import com.example.dicodingstoryappv1.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): RegisterResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token : String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): GetAllStoryResponse

    @GET("stories")
    suspend fun getMarker(
        @Header("Authorization") token : String,
        @Query("location") location: Int
    ): GetAllStoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") Authorization: String,
        @Part file : MultipartBody.Part,
        @Part("description") description : RequestBody,

        //optional
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): AddNewStoryResponse
}