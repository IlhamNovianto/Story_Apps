package com.example.dicodingstoryappv1.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstoryappv1.R
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.api.response.UserPlace
import com.example.dicodingstoryappv1.databinding.ActivityMapsBinding
import com.example.dicodingstoryappv1.factoryViewModel.StoryFactoryViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var token: String
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        setupViewModel()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupViewModel() {
        val factory: StoryFactoryViewModel = StoryFactoryViewModel.getInstance(this)
        mapsViewModel = ViewModelProvider(this, factory) [MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setDataMaps()
        getMyLocation()
        setMapsyte()
    }
    private fun setDataMaps() {
        mapsViewModel.getToken().observe(this) {token ->
            if (token.isNotEmpty()) {
                mapsViewModel.getStoryMaps(token, 1).observe(this){result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val userPlace = result.data.listStory.map {
                                    UserPlace(
                                        it.name,
                                        it.lat,
                                        it.lon
                                    )
                                }
                                userPlace.forEach { user ->
                                    if (user.lat != null && user.lon != null) {
                                        val latLng = LatLng(user.lat, user.lon)
                                        mMap.addMarker(MarkerOptions().position(latLng).title(user.userName))
                                        boundsBuilder.include(latLng)
                                    }

                                    val bounds: LatLngBounds = boundsBuilder.build()
                                    mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngBounds(
                                            bounds,
                                            resources.displayMetrics.widthPixels,
                                            resources.displayMetrics.heightPixels,
                                            300
                                        )
                                    )
                                }
                            }

                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setMapsyte() {
        try {
            val succses =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!succses) {
                Log.e(TAG, "Style Parshing Failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Cant find style. Error: ", exception)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
    private fun showLoading(b: Boolean) {
        View.GONE
        View.VISIBLE
    }


    companion object {
        private const val TAG = "MapsActivity"
        const val EXTRA_TOKEN = "extra_token"
    }
}