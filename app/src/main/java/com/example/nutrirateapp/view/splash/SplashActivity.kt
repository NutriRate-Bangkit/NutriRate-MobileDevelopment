package com.example.nutrirateapp.view.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nutrirateapp.R
import com.example.nutrirateapp.data.pref.UserPreferences
import com.example.nutrirateapp.view.Onboarding.OnboardingActivity
import com.example.nutrirateapp.view.main.MainActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        userPreferences = UserPreferences(this)

        val appName = findViewById<ImageView>(R.id.app_name)
        val animator = ObjectAnimator.ofFloat(appName, "translationY", -500f, 0f)
        animator.duration = 3000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            checkSession()
        }, 3000)
    }

    private fun checkSession() {
        lifecycleScope.launch {
            userPreferences.getSession().collect { isLoggedIn ->
                val intent = if (isLoggedIn) {
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, OnboardingActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}