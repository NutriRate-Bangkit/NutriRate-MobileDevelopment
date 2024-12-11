package com.example.nutrirateapp.view.grading

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.nutrirateapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class GradingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grading)

        val productCard = findViewById<CardView>(R.id.productCard)
        val hiddenGrades = findViewById<View>(R.id.hiddenGrades)
        val bottomSheetBehavior = BottomSheetBehavior.from(productCard)

        bottomSheetBehavior.peekHeight = 1650
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        hiddenGrades.visibility = View.GONE

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> animateFadeIn(hiddenGrades)
                    BottomSheetBehavior.STATE_COLLAPSED -> animateFadeOut(hiddenGrades)
                    else -> Unit
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
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
