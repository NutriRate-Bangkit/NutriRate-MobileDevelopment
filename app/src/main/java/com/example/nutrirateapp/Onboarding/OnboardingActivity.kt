package com.example.nutrirateapp.Onboarding

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
                "Lorem Ipsum Lorem Ipsum Lorem Ipsum",
                false
            ),
            OnboardingFragment.newInstance(
                R.drawable.nutri_onboarding2,
                "Scan Makanan Anda",
                "Lorem Ipsum Lorem Ipsum Lorem Ipsum",
                false
            ),
            OnboardingFragment.newInstance(
                R.drawable.nutri_onboarding3,
                "Nikmati Kemudahan",
                "Lorem Ipsum Lorem Ipsum Lorem Ipsum",
                true
            )
        )

        val adapter = OnboardingAdapter(this, fragments)
        viewPager.adapter = adapter
   }
}