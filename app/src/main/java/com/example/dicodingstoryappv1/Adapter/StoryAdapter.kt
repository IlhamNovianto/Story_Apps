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
import com.example.dicodingstoryappv1.api.entity.StoryEntity
import com.example.dicodingstoryappv1.databinding.CardUserBinding
import com.example.dicodingstoryappv1.ui.detail.DetailActivity

class StoryAdapter: PagingDataAdapter<StoryEntity, StoryAdapter.ListViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            CardUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position) //getItem = StoryEntity?
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder (private val binding: CardUserBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(story: StoryEntity) {
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {

            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}