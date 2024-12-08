package com.example.nutrirateapp.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Deklarasi variabel untuk binding dan NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout menggunakan View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation Component
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Inisialisasi NavController
        navController = navHostFragment.navController

        // Hubungkan Bottom Navigation dengan NavController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    // Optional: Override method untuk menangani tombol back
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}