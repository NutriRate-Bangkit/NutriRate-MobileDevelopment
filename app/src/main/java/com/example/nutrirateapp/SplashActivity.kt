package com.example.nutrirateapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Animasi gerakan teks app_name
        val appName = findViewById<TextView>(R.id.app_name)
        val animator = ObjectAnimator.ofFloat(appName, "translationY", -500f, 0f)
        animator.duration = 1500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()

        // Pindah ke MainActivity setelah animasi selesai
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // Delay 2 detik
    }
}