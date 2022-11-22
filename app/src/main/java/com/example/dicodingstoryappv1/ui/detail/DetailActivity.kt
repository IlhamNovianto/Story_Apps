@file:Suppress("DEPRECATION")

package com.example.dicodingstoryappv1.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.dicodingstoryappv1.api.response.ListStoryItem
import com.example.dicodingstoryappv1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDetail()

        supportActionBar?.title ="Detail Story"
        supportActionBar?. setDisplayHomeAsUpEnabled(true)

    }

    private fun setupDetail(){
        val story = intent.getParcelableExtra<ListStoryItem>(DETAIL)
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.imgDetail)
        binding.userNameDetail.text = story?.name
        binding.userDescDetail.text = story?.description
    }

    companion object {
        const val DETAIL = "DETAIL"
    }
}