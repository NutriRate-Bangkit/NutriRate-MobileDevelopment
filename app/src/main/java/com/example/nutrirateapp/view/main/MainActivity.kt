package com.example.nutrirateapp.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.ActivityMainBinding
import com.example.nutrirateapp.view.camera.CameraActivity
import com.example.nutrirateapp.view.profile.ProfileActivity
import com.example.nutrirateapp.view.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProfileViewModel by viewModels()

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupObservers()
        viewModel.getProfile()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfile()
    }

    @OptIn(ExperimentalGetImage::class)
    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
        }

        binding.cdvPhoto.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.profileResult.observe(this) { result ->
            result.fold(
                onSuccess = { profile ->
                    binding.apply {
                        tvName.text = "Hi! ${profile.name}"

                        Glide.with(this@MainActivity)
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
    }
}
