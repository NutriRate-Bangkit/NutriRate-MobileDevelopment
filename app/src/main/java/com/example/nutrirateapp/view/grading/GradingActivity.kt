package com.example.nutrirateapp.view.grading

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.data.model.OriginalInputs
import com.example.nutrirateapp.databinding.ActivityGradingBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class GradingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGradingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomSheet()
        displayGradeAndInputs()
    }

    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.productCard)
        bottomSheetBehavior.peekHeight = 1650
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.hiddenGrades.visibility = View.GONE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> animateFadeIn(binding.hiddenGrades)
                    BottomSheetBehavior.STATE_COLLAPSED -> animateFadeOut(binding.hiddenGrades)
                    else -> Unit
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun displayGradeAndInputs() {
        intent?.let { intent ->
            // Display Grade
            val grade = intent.getStringExtra("GRADE") ?: "N/A"
            binding.gradeTextView.apply {
                text = grade
                setTextColor(getGradeColor(grade))
            }

            val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown Product"
            binding.productNameTextView.text = productName

            val gramPerServing = intent.getDoubleExtra("GRAM_PER_SERVING", 0.0)
            binding.servingValue.text = gramPerServing.toString()

            val originals = intent.getParcelableExtra<OriginalInputs>("ORIGINAL_INPUTS")
            originals?.let {
                binding.apply {
                    proteinValue.text = it.protein.toString()
                    energyValue.text = it.energy.toString()
                    fatValue.text = it.fat.toString()
                    saturatedFatValue.text = it.saturatedFat.toString()
                    sugarValue.text = it.sugars.toString()
                    fiberValue.text = it.fiber.toString()
                    sodiumValue.text = it.sodium.toString()
                }
            }
        }
    }

    private fun getGradeColor(grade: String): Int {
        return when (grade) {
            "A" -> Color.parseColor("#008000")  // Green
            "B" -> Color.parseColor("#FFD700")  // Gold
            "C" -> Color.parseColor("#FFA500")  // Orange
            "D" -> Color.parseColor("#FF4500")  // Orange Red
            "E" -> Color.parseColor("#FF0000")  // Red
            else -> Color.BLACK
        }
    }

    private fun animateFadeIn(view: View) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 500
            fadeIn.fillAfter = true
            view.startAnimation(fadeIn)
        }
    }

    private fun animateFadeOut(view: View) {
        if (view.visibility == View.VISIBLE) {
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.duration = 500
            fadeOut.fillAfter = true
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    view.visibility = View.GONE
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            view.startAnimation(fadeOut)
        }
    }
}