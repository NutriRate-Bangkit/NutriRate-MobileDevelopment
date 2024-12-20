package com.example.nutrirateapp.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.ActivityProfileBinding
import com.example.nutrirateapp.view.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupBackButton()
        setupMenuButtons()
        setupLogoutButton()
        setupObservers()
        viewModel.getProfile()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupMenuButtons() {
        binding.apply {
            tvEditProfile.setOnClickListener {
                EditProfileDialogFragment().show(supportFragmentManager, "EditProfileDialog")
            }

            tvChangePassword.setOnClickListener {
                val dialog = ChangePasswordFragment()
                dialog.show(supportFragmentManager, "ChangePasswordDialog")
            }

            tvDeleteAccount.setOnClickListener {
                DeleteAccountDialog().show(supportFragmentManager, "DeleteAccountDialog")
            }
        }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.profileResult.observe(this) { result ->
            result.fold(
                onSuccess = { profile ->
                    binding.apply {
                        tvName.text = profile.name
                        tvEmail.text = profile.email
                        Glide.with(this@ProfileActivity)
                            .load(profile.image)
                            .circleCrop()
                            .placeholder(R.drawable.img_user)
                            .error(R.drawable.img_user)
                            .into(ivProfileImage)
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(
                        this,
                        "Failed to load profile: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        viewModel.logoutResult.observe(this) { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                },
                onFailure = { exception ->
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnLogout.isEnabled = !isLoading
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
