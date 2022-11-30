package com.example.dicodingstoryappv1.api.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story_entity")
@Parcelize
data class StoryEntity(
    @field:SerializedName("id")
    @PrimaryKey
    val id: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double
): Parcelable