package com.example.dicodingstoryappv1.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstoryappv1.api.response.ListStoryItem
import com.example.dicodingstoryappv1.databinding.CardUserBinding
import com.example.dicodingstoryappv1.ui.detail.DetailActivity

class StoryAdapter:
    PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK){
    //cara Lain dari Object constructur di atas adalah :private val story = ArrayList<CardStory>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            CardUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder (private val binding: CardUserBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivAvatarIncard)
            binding.apply {
                tvNameIncard.text = story.name
                tvDescIncard.text = story.description
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DETAIL, story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.tvNameIncard, "name"),
                        Pair(binding.ivAvatarIncard, "image"),
                        Pair(binding.tvDescIncard, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}