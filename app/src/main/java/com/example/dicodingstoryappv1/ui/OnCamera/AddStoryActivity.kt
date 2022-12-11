package com.example.dicodingstoryappv1.ui.OnCamera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstoryappv1.Utils.reduceFileImage
import com.example.dicodingstoryappv1.Utils.uriToFile
import com.example.dicodingstoryappv1.Utils.createTempFile
import com.example.dicodingstoryappv1.R
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.databinding.ActivityAddStoryBinding
import com.example.dicodingstoryappv1.ui.listStory.ListStoryActivity
import com.example.dicodingstoryappv1.ui.login.MainActivity
import com.example.dicodingstoryappv1.ui.maps.MapsActivity
import com.example.dicodingstoryappv1.factoryViewModel.StoryFactoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStoryActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var token: String
    private var getFile: File? = null

    private var _latitude: Double? = null
    private var _longitude: Double? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResult: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(
                this@AddStoryActivity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        token = intent.getStringExtra(MapsActivity.EXTRA_TOKEN).toString()

        this.title = "add story"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupViewModel()
        getUserLocation()
        binding.apply {
            btnGallery.setOnClickListener{
                openGalerry()
            }
            btnCamera.setOnClickListener{
                takePhoto()
            }
            btnUpload.setOnClickListener{
                uploadPhoto()
            }
        }
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, getString(R.string.text_location_must_enabled), Toast.LENGTH_LONG)
                .show()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        _latitude = location.latitude
                        _longitude = location.longitude
                        Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        _latitude ?: String()
                        _longitude ?: String()
                        Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

    private fun setupViewModel() {
        val factory: StoryFactoryViewModel = StoryFactoryViewModel.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]

        viewModel.getToken().observe(this){ token ->
            if (token.isEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                this.token = token
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.dicodingstoryappv1",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result =BitmapFactory.decodeFile(getFile?.path)
            binding.imgAddPreview.setImageBitmap(result)
        }
    }

    private fun uploadPhoto() {
        if (getFile != null) {
            val desc = binding.etDescriptionStory.text.toString()
            if (desc.isEmpty()) {
                binding.etDescriptionStory.error = resources.getString(R.string.null_desc_msg)
            } else {
                val file = reduceFileImage(getFile as File)
                val description = desc.toRequestBody("text/plain".toMediaType())
                val requestImg = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImg
                )
                viewModel.addStory(
                    token,
                    imgMultipart,
                    description,
                    _latitude?.toString(),
                    _longitude?.toString()
                ).observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                Toast.makeText(
                                    this,
                                    getString(R.string.upload_sucsessful_msg),
                                    Toast.LENGTH_SHORT).show()
                                moveToUserStory()
                                finish()
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    this,
                                    getString(R.string.upload_failed_msg) + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun moveToUserStory() {
        Intent(this@AddStoryActivity, ListStoryActivity::class.java)
        finish()
    }

    private fun openGalerry() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choosepicture))
        launcherIntentGallery.launch(chooser)
    }

   private val launcherIntentGallery = registerForActivityResult(
       ActivityResultContracts.StartActivityForResult()
   ){
       if (it.resultCode == RESULT_OK) {
           val selectedImg: Uri = it.data?.data as Uri
           val myPicture = uriToFile(selectedImg, this)
           getFile = myPicture
           binding.imgAddPreview.setImageURI(selectedImg)
       }
   }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }


}