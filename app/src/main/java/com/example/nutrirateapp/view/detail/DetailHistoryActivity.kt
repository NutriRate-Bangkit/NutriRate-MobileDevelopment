package com.example.nutrirateapp.view.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.data.model.HistoryItem
import com.example.nutrirateapp.databinding.ActivityDetailHistoryBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding
    private val viewModel: DetailHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupBottomSheet()

        val historyItem = intent.getParcelableExtra<HistoryItem>("HISTORY_ITEM")
        historyItem?.let {
            viewModel.setHistoryDetail(it)
        }
    }

    private fun setupObservers() {
        viewModel.historyDetail.observe(this) { history ->
            binding.apply {
                gradeTextView.apply {
                    text = history.grade
                    setTextColor(getGradeColor(history.grade))
                }

                productNameDetail.text = history.productName
                scannedByUser.text = history.name

                history.originalInputs.let { inputs ->
                    servingValue.text = history.gramPerServing.toString()
                    proteinValue.text = inputs.protein.toString()
                    energyValue.text = inputs.energy.toString()
                    fatValue.text = inputs.fat.toString()
                    saturatedFatValue.text = inputs.saturatedFat.toString()
                    sugarValue.text = inputs.sugars.toString()
                    fiberValue.text = inputs.fiber.toString()
                    saltValue.text = inputs.sodium.toString()
                }
            }
        }
    }


    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.productCardDetail)
        bottomSheetBehavior.apply {
            peekHeight = 1650
            state = BottomSheetBehavior.STATE_COLLAPSED

            binding.hiddenGradesDetail.visibility = View.GONE

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> animateFadeIn(binding.hiddenGradesDetail)
                        BottomSheetBehavior.STATE_COLLAPSED -> animateFadeOut(binding.hiddenGradesDetail)
                        else -> Unit
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        binding.okButton.setOnClickListener {
            finish()
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