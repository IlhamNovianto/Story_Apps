package com.example.dicodingstoryappv1.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstoryappv1.R
import com.example.dicodingstoryappv1.databinding.ActivityRegisterBinding
import com.example.dicodingstoryappv1.ui.login.MainActivity
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.factoryViewModel.UserFactoryViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailFocusListner()
        passFocusListner()


        setupView()
        setupViewModel()
        setAnimation()
        setupAction()
    }


    //funtion fullscreen
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
        registerViewModel = ViewModelProvider(this, factory) [RegisterViewModel::class.java]
    }

    //funtion animation
    private fun setAnimation() {
        val regisTitle = ObjectAnimator.ofFloat(binding.regisTitle, View.ALPHA, 1f).setDuration(500)
        val btnRegisName = ObjectAnimator.ofFloat(binding.tilName, View.ALPHA, 1f).setDuration(500)
        val btnRegisEmail = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val btnRegisPassword = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegis2 = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(regisTitle, btnRegisName, btnRegisEmail, btnRegisPassword, btnRegis2)
            start()
        }
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
        val regisName = binding.etRegisName.text.toString()
        val regisEmail = binding.etRegisEmail.text.toString()
        val regisPass = binding.etRegisPassword.text.toString()

        when {
            regisName.isEmpty() -> {
                binding.etRegisName.error = resources.getString(R.string.name_empty_msg)
            }
            regisEmail.isEmpty() -> {
                binding.etRegisEmail.error = resources.getString(R.string.wrong_format_email)
            }
            regisPass.isEmpty() -> {
                binding.etRegisPassword.error = resources.getString(R.string.password_invalid_msg)
            } else -> {
                registerViewModel.register(regisName, regisEmail, regisPass)
                    .observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    startActivity(Intent(this@RegisterActivity,
                                        MainActivity::class.java))
                                    finish()
                                    Toast.makeText(this@RegisterActivity,
                                        getString(R.string.registerBerhasil), Toast.LENGTH_SHORT)
                                        .show()
                                }
                                else -> {
                                    Toast.makeText(this@RegisterActivity,
                                        getString(R.string.registergagal), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
            }
        }
    }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //Email Validation
    private fun emailFocusListner() {
        binding.etRegisEmail.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.tilEmail.helperText = validEmail()
            }
        }
    }
    private fun validEmail(): String? {
        val emailText = binding.etRegisEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Invalid Email"
        }
        return null
    }

    //Password Validation
    private fun passFocusListner() {
        binding.etRegisPassword.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.tilPassword.helperText = validPass()
            }
        }
    }
    private fun validPass(): String? {
        val password = binding.etRegisPassword.text.toString()
        if (password.length < 6 ){
            return "Password Must be 6 Character"
        }
        if (password.length > 6 ){
            return "Password Must be 6 Character"
        }
        return null
    }
}
