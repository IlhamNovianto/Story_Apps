package com.example.dicodingstoryappv1.widget

import android.content.Context
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.net.toUri
import com.example.dicodingstoryappv1.R
import com.example.dicodingstoryappv1.Utils.toBitmap
import com.example.dicodingstoryappv1.api.entity.StoryEntity


class StackRemoteViewsFactory(private val mContext: Context):
RemoteViewsService.RemoteViewsFactory {

    private val databaseAuthority = "com.example.dicodingstoryappv1"
    private val userTableName = "story_entity"
    private val databaseScheme = "content"
    private val databaseContentUri = "$databaseScheme://$databaseAuthority"
    private val userContentUri = "$databaseContentUri/$userTableName"

    private var list: List<StoryEntity> = listOf()
    private var cursor: Cursor? = null

    internal class StoryColumns {
        companion object{
            const val ID = "id"
            const val NAME = "name"
            const val DESCRIPTION = "description"
            const val PHOTO_URL = "photoUrl"
            const val CREATED_AT = "createdAt"
            const val LAT = "lat"
            const val LON = "lon"
        }
    }


    override fun onCreate() {}

    private fun Cursor.mapCursorToArrayList(): ArrayList<StoryEntity>{
        val list = ArrayList<StoryEntity>()
        while (moveToNext()) {
            val id =
                getString(getColumnIndexOrThrow(StoryColumns.ID))
            val name =
                getString(getColumnIndexOrThrow(StoryColumns.NAME))
            val description =
                getString(getColumnIndexOrThrow(StoryColumns.DESCRIPTION))
            val photoUrl =
                getString(getColumnIndexOrThrow(StoryColumns.PHOTO_URL))
            val createdAt =
                getString(getColumnIndexOrThrow(StoryColumns.CREATED_AT))
            val lat =
                getString(getColumnIndexOrThrow(StoryColumns.LAT))
            val lon =
                getString(getColumnIndexOrThrow(StoryColumns.LON))
            list.add(
                StoryEntity(
                    id,
                    photoUrl,
                    name,
                    description,
                    createdAt,
                    lat.toDouble(),
                    lon.toDouble()
                )
            )
        }
        return list
    }


    override fun onDataSetChanged() {
        cursor?.close()
        val identifyTeken = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver?.query(userContentUri.toUri(), null,null,null,null)
        cursor?.let {
            list = it. mapCursorToArrayList()
        }
        Binder.restoreCallingIdentity(identifyTeken)
    }

    override fun onDestroy() {
        cursor?.close()
        list = listOf()
    }

    override fun getCount(): Int = list.size



    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        if(list.isNotEmpty()) {
            rv.apply {
                list[position].apply {
                    setImageViewBitmap(
                        R.id.widget_image_view, photoUrl?.toBitmap(mContext)
                    )
                    setTextViewText(
                        R.id.widget_username, name
                    )
                    setTextViewText(
                        R.id.description, description
                    )
                }
            }
        }
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}