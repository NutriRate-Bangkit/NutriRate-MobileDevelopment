package com.example.nutrirateapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.ActivityMainBinding
import com.example.nutrirateapp.view.camera.CameraActivity
import com.example.nutrirateapp.view.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavController
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Hubungkan BottomNavigationView dengan NavController
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)

        // Custom handling untuk FloatingActionButton (FAB)
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
}
