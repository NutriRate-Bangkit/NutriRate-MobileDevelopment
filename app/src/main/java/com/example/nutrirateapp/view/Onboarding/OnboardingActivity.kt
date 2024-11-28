package com.example.nutrirateapp.view.Onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.nutrirateapp.R

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val fragments = listOf(
            OnboardingFragment.newInstance(
                R.drawable.nutri_onboarding1,
                "Grading Makanan dengan Mudah",
                "NutriRate membantu Anda menilai makanan berdasarkan Nutrition Facts. Temukan pilihan terbaik untuk pola makan sehat Anda.",
                false
            ),
            OnboardingFragment.newInstance(
                R.drawable.nutri_onboarding2,
                "Analisis Gizi Instan",
                "Scan label Nutrition Facts dan dapatkan informasi yang mudah dipahami, lengkap dengan rekomendasi kesehatan.",
                false
            ),
            OnboardingFragment.newInstance(
                R.drawable.nutri_onboarding3,
                "Personalisasi Gaya Hidup Sehat Anda",
                "Sesuaikan kebutuhan nutrisi Anda dengan rekomendasi yang dipersonalisasi berdasarkan preferensi dan tujuan kesehatan Anda.",
                true
            )
        )

        val adapter = OnboardingAdapter(this, fragments)
        viewPager.adapter = adapter
   }
}