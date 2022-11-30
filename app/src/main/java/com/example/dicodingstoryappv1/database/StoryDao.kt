package com.example.dicodingstoryappv1.database

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingstoryappv1.api.entity.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntity?>?)

    @Query("SELECT * FROM story_entity")
    fun getAllstories(): PagingSource<Int, StoryEntity>

    //for stack widget
    @Query("SELECT * FROM story_entity")
    fun getAllStoryWidget(): Cursor

    @Query("DELETE FROM story_entity")
    fun deleteAll()
}