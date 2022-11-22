package com.example.dicodingstoryappv1.ui.listStory


import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstoryappv1.Adapter.LoadingStateAdapter
import com.example.dicodingstoryappv1.Adapter.StoryAdapter
import com.example.dicodingstoryappv1.R
import com.example.dicodingstoryappv1.databinding.ActivityListStoryBinding
import com.example.dicodingstoryappv1.ui.OnCamera.AddStoryActivity
import com.example.dicodingstoryappv1.ui.maps.MapsActivity
import com.example.dicodingstoryappv1.ui.login.MainActivity
import com.example.dicodingstoryappv1.factoryViewModel.StoryFactoryViewModel


class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var listStoryViewModel: ListStoryViewModel

    //fLOATING bUTTON
    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.fab_open) }
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.fab_close) }
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.from_bottom) }
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.to_button) }
    private var clicked = false
    //fLOATING bUTTON


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityListStoryBinding.inflate(layoutInflater)
            setContentView(binding.root)

            supportActionBar?.title = getString(R.string.story_user)

            setUpViewModel()

            binding.fabAction.setOnClickListener{
                onCickActionFab()
            }
            binding.fabAddStory.setOnClickListener{
                startActivity(Intent(this, AddStoryActivity::class.java))
            }
            binding.fabMaps.setOnClickListener {
                startActivity(Intent(this, MapsActivity::class.java))
            }

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(this, "location enabled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Location must be enabled", Toast.LENGTH_LONG).show()
        }
    }


    private fun setUpViewModel() {
        if (application.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 1)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }

        val factory: StoryFactoryViewModel = StoryFactoryViewModel.getInstance(this)
        listStoryViewModel = ViewModelProvider(this, factory)[ListStoryViewModel::class.java]
        listStoryViewModel.isLogin().observe(this) {
            if (!it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        listStoryViewModel.getToken().observe(this) {token ->
            if (token.isNotEmpty()) {
                if (token.isNotEmpty()) {
                    val adapter = StoryAdapter()
                    binding.rvUser.adapter = adapter.withLoadStateFooter(
                        footer = LoadingStateAdapter {
                            adapter.retry()
                        }
                    )
                    listStoryViewModel.getStory(token).observe(this) {result ->
                        adapter.submitData(lifecycle, result)
                    }
                }
            }
        }
    }

    //FLOATING BUTTON
    private fun onCickActionFab() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = (!clicked)
    }

    private fun setVisibility(clicked: Boolean) {
     if(!clicked){
         binding.fabAddStory.visibility = View.VISIBLE
         binding.fabMaps.visibility = View.VISIBLE
     }else{
         binding.fabAddStory.visibility = View.GONE
         binding.fabMaps.visibility = View.GONE
     }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked){
            binding.fabAddStory.startAnimation(fromBottom)
            binding.fabMaps.startAnimation(fromBottom)
            binding.fabAction.startAnimation(rotateOpen)
        }else{
            binding.fabAddStory.startAnimation(toBottom)
            binding.fabMaps.startAnimation(toBottom)
            binding.fabAction.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean){
        if (!clicked){
            binding.fabAddStory.isClickable = true
            binding.fabMaps.isClickable = true
        } else{
            binding.fabAddStory.isClickable = false
            binding.fabMaps.isClickable = false
        }
    }
    //FLOATING BUTTON

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.logout -> {
                listStoryViewModel.logout()
                true
            }
            R.id.settingLanguage -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
    }