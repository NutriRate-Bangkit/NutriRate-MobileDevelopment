package com.example.nutrirateapp.view.result

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setNutritionValues()
        setupGradingButton()
        setupObservers()
    }

    private fun setupGradingButton() {
        binding.gradingButton.setOnClickListener {
            Log.d("ResultActivity", "Grading button clicked")

            if (validateInput()) {
                val productName = binding.productNameEditText.text.toString()
                val takaranSaji = binding.servingEditText.text.toString().toDoubleOrNull() ?: 0.0
                val protein = binding.proteinEditText.text.toString().toDoubleOrNull() ?: 0.0
                val energyKkal = binding.energyEditText.text.toString().toDoubleOrNull() ?: 0.0
                val fat = binding.fatEditText.text.toString().toDoubleOrNull() ?: 0.0
                val saturatedFat = binding.saturatedFatEditText.text.toString().toDoubleOrNull() ?: 0.0
                val sugars = binding.sugarEditText.text.toString().toDoubleOrNull() ?: 0.0
                val fiber = binding.fiberEditText.text.toString().toDoubleOrNull() ?: 0.0
                val sodium = binding.sodiumEditText.text.toString().toDoubleOrNull() ?: 0.0

                Log.d("ResultActivity", """
                   Collected values:
                   Product Name: $productName
                   Takaran Saji: $takaranSaji
                   Protein: $protein
                   Energy: $energyKkal
                   Fat: $fat
                   Saturated Fat: $saturatedFat
                   Sugars: $sugars
                   Fiber: $fiber
                   Sodium: $sodium
               """.trimIndent())

                viewModel.predictGrade(
                    productName = productName,
                    takaranSaji = takaranSaji,
                    protein = protein,
                    energyKkal = energyKkal,
                    fat = fat,
                    saturatedFat = saturatedFat,
                    sugars = sugars,
                    fiber = fiber,
                    sodium = sodium
                )
            }
        }
    }

    private fun validateInput(): Boolean {
        Log.d("ResultActivity", "Validating input")

        val productName = binding.productNameEditText.text.toString()
        if (productName.isEmpty()) {
            Toast.makeText(this, "Nama produk harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        val takaranSaji = binding.servingEditText.text.toString().toDoubleOrNull()
        if (takaranSaji == null || takaranSaji <= 0) {
            Toast.makeText(this, "Takaran saji harus diisi dengan benar", Toast.LENGTH_SHORT).show()
            return false
        }

        Log.d("ResultActivity", "Input validation successful")
        return true
    }

    private fun setupObservers() {
        viewModel.predictionResult.observe(this) { result ->
            result.fold(
                onSuccess = { response ->
                    Log.d("ResultActivity", "Prediction success: Grade = ${response.grade}")
                    Toast.makeText(this, "Grading berhasil", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Log.e("ResultActivity", "Prediction error: ${exception.message}")
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.gradingButton.isEnabled = !isLoading
        }
    }

    private fun setNutritionValues() {
        Log.d("ResultActivity", "Setting nutrition values from intent")

        intent.apply {
            binding.proteinEditText.setText(getStringExtra("PROTEIN") ?: "Not found")
            binding.energyEditText.setText(getStringExtra("ENERGY") ?: "Not found")
            binding.fatEditText.setText(getStringExtra("FAT") ?: "Not found")
            binding.saturatedFatEditText.setText(getStringExtra("SATURATED_FAT") ?: "Not found")
            binding.sugarEditText.setText(getStringExtra("SUGAR") ?: "Not found")
            binding.fiberEditText.setText(getStringExtra("FIBER") ?: "Not found")
            binding.sodiumEditText.setText(getStringExtra("SODIUM") ?: "Not found")
        }

        Log.d("ResultActivity", "Nutrition values set from intent")
    }
}