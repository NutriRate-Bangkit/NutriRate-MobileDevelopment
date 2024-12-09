package com.example.nutrirateapp.view.register

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.ActivityRegisterBinding
import com.example.nutrirateapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupClickableTexts()
        setupRegisterButton()
        setupObservers()
    }

    private fun setupClickableTexts() {
        binding.tvSudahPunyaAkun.setOnClickListener {
            navigateToLogin()
        }

        // Setup clickable text "disini"
        val text = getString(R.string.regis_kitakenalan)
        val spannableString = SpannableString(text)

        // Find index of "disini" in the text
        text.indexOf("disini").let { startIndex ->
            if (startIndex >= 0) {
                val endIndex = startIndex + "disini".length

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        navigateToLogin()
                    }
                }

                spannableString.setSpan(
                    clickableSpan,
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        // Apply the spannable text to descriptionText
        binding.descriptionText.apply {
            setText(spannableString)
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setupRegisterButton() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(name, email, password)) {
                viewModel.registerUser(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.nameEditText.error = "Nama wajib diisi"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email wajib diisi"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password wajib diisi"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordEditText.error = "Password minimal 6 karakter"
            isValid = false
        }

        return isValid
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.registerResult.observe(this) { result ->
            result.fold(
                onSuccess = { response ->
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    navigateToLoginAfterRegister()
                },
                onFailure = { exception ->
                    Toast.makeText(
                        this,
                        "Registration failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            registerButton.isEnabled = !isLoading
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun navigateToLoginAfterRegister() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}