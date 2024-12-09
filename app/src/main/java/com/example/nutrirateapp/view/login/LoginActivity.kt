package com.example.nutrirateapp.view.login

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
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.ActivityLoginBinding
import com.example.nutrirateapp.view.main.MainActivity
import com.example.nutrirateapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupClickableTexts()
        setupLoginButton()
        setupObservers()
    }

    private fun setupClickableTexts() {
        // Setup "Belum punya akun" clickable text
        binding.tvBelumPunyaAkun.setOnClickListener {
            navigateToRegister()
        }

        // Setup "disini" clickable text in description
        val text = getString(R.string.login_selamatdatang)
        val spannableString = SpannableString(text)

        text.indexOf("disini").let { startIndex ->
            if (startIndex >= 0) {
                val endIndex = startIndex + "disini".length

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        navigateToRegister()
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

        binding.descriptionText.apply {
            setText(spannableString)
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(email, password)) {
                viewModel.loginUser(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email wajib diisi"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password wajib diisi"
            isValid = false
        }

        return isValid
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.loginResult.observe(this) { result ->
            result.fold(
                onSuccess = { response ->
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    navigateToMain()
                },
                onFailure = { exception ->
                    Toast.makeText(
                        this,
                        "Login failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            loginButton.isEnabled = !isLoading
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}