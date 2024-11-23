package com.example.nutrirate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Set delay menggunakan Handler, misalnya 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            // Setelah 3 detik, pindah ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000ms = 3 detik
    }
}