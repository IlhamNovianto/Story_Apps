package com.example.dicodingstoryappv1.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.dicodingstoryappv1.database.StoryDao
import com.example.dicodingstoryappv1.database.StoryDatabase

class StoryContentProvider : ContentProvider() {

    private lateinit var storyDao: StoryDao

    override fun onCreate(): Boolean {
        storyDao = context?.let { ctx ->
            StoryDatabase.getDatabase(ctx).databaseDao()
        }!!
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        val cursor: Cursor?
        when(uriMatcher.match(uri)){
            ID_FAVORITE ->{
                cursor = storyDao.getAllStoryWidget()
                if (null != context) {
                    cursor.setNotificationUri(context?.contentResolver, uri)
                }
            }
            else -> {
                cursor = null
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        return 0
    }

    companion object {
        private const val AUTHORITY = "com.example.dicodingstoryappv1"
        private const val TABLE_NAME = "story_entity"
        const val ID_FAVORITE = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, ID_FAVORITE)
        }
    }

}