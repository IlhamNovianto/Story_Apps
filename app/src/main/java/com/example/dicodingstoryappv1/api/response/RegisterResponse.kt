package com.example.dicodingstoryappv1.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)