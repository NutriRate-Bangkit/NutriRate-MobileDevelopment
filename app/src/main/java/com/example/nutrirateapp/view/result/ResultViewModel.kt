package com.example.nutrirateapp.view.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrirateapp.data.model.PredictionResponse
import com.example.nutrirateapp.data.repository.GradingRepository
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GradingRepository(application)

    private val _predictionResult = MutableLiveData<Result<PredictionResponse>>()
    val predictionResult: LiveData<Result<PredictionResponse>> = _predictionResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun predictGrade(
        productName: String,
        gramPerServing: Double,
        protein: Double,
        energy: Double,
        fat: Double,
        saturatedFat: Double,
        sugars: Double,
        fiber: Double,
        sodium: Double
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.predictGrade(
                    productName = productName,
                    gramPerServing = gramPerServing,
                    protein = protein,
                    energy = energy,
                    fat = fat,
                    saturatedFat = saturatedFat,
                    sugars = sugars,
                    fiber = fiber,
                    sodium = sodium
                )
                _predictionResult.value = result
            } catch (e: Exception) {
                _predictionResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}