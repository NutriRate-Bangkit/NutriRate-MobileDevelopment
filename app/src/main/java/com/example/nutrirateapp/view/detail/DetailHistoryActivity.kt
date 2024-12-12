package com.example.nutrirateapp.view.detail

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.databinding.ActivityDetailHistoryBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewBinding
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown Product"
        val grade = intent.getStringExtra("GRADE") ?: "Unknown Grade"
        val user = intent.getStringExtra("USER") ?: "User"

        // Tampilkan data di TextView menggunakan binding
        binding.productNameDetail.text = productName
        binding.gradeTextView.text = grade
        binding.scannedByUser.text = user

        // Atur tombol OK untuk kembali
        binding.okButton.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        // Ambil BottomSheet dan elemen tersembunyi
        bottomSheetBehavior = BottomSheetBehavior.from(binding.productCardDetail)
        val hiddenGrades = binding.hiddenGradesDetail

        // Set initial state
        bottomSheetBehavior.peekHeight = 1680
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        hiddenGrades.visibility = View.GONE

        // Tambahkan callback untuk perubahan state BottomSheet
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> animateFadeIn(hiddenGrades)
                    BottomSheetBehavior.STATE_COLLAPSED -> animateFadeOut(hiddenGrades)
                    else -> Unit
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Tidak ada perubahan pada slideOffset
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
