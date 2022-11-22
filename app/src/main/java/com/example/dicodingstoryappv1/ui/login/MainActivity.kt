package com.example.dicodingstoryappv1.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstoryappv1.R

import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.databinding.ActivityMainBinding
import com.example.dicodingstoryappv1.ui.listStory.ListStoryActivity
import com.example.dicodingstoryappv1.ui.register.RegisterActivity
import com.example.dicodingstoryappv1.factoryViewModel.UserFactoryViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailFocusListner()
        passFocusListner()

        //Caranya hampir sama ketika selesai register langsung ke menu utama login, dan tidak bisa kembali ke menu register
        setupView()
        setupViewModel()
        setupAction()
        setAnimation()


    }

    //fullscreen
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: UserFactoryViewModel = UserFactoryViewModel.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory) [LoginViewModel::class.java]
        loginViewModel.getToken().observe(this) {token ->
            if (token.isNotEmpty()) {
                startActivity(Intent(this, ListStoryActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val loginEmail = binding.etLoginEmail.text.toString()
            val loginPass = binding.etLoginPassword.text.toString()
            when {
                loginEmail.isEmpty() -> {
                    binding.etLoginEmail.error = "Email must be filled"
                }
                loginPass.isEmpty() -> {
                    binding.etLoginPassword.error = "Password must Be Filled"
                }
                else -> {
                    loginViewModel.login(loginEmail, loginPass).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error == true) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Login Sucsessful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val token = data.loginResult?.token ?: ""
                                        loginViewModel.setToken(token, true)
                                    }
                                }
                                is Result.Error -> {
                                    Toast.makeText(this@MainActivity,
                                        "Login Failed",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.btnRegister1.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setAnimation() {
        val title = ObjectAnimator.ofFloat(binding.storyApp, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.subtitle, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val btnRegister1 = ObjectAnimator.ofFloat(binding.btnRegister1, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, subtitle, btnLogin, btnRegister1)
            startDelay = 500
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//Email Validation
    private fun emailFocusListner() {
        binding.etLoginEmail.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.textInputLayout.helperText = validEmail()
            }
        }
    }
    private fun validEmail(): String? {
        val emailText = binding.etLoginEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Invalid Email"
        }
        return null
    }

//Password Validation
    private fun passFocusListner() {
        binding.etLoginPassword.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.textInputLayout2.helperText = validPass()
            }
        }
    }
    private fun validPass(): String? {
        return null
    }
}



