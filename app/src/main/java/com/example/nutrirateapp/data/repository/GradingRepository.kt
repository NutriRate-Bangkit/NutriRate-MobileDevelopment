package com.example.nutrirateapp.data.repository

import android.content.Context
import com.example.nutrirateapp.data.model.PredictionResponse
import com.example.nutrirateapp.data.nutritioncalculate.NutritionCalculate
import com.example.nutrirateapp.data.pref.UserPreferences
import com.example.nutrirateapp.data.retrofitAPI.APIconfig
import kotlinx.coroutines.flow.first

class GradingRepository(private val context: Context) {
    private val userPreferences = UserPreferences(context)
    private var apiService = APIconfig.getApiService()

    suspend fun predictGrade(
        productName: String,
        takaranSaji: Double,
        protein: Double,
        energyKkal: Double,
        fat: Double,
        saturatedFat: Double,
        sugars: Double,
        fiber: Double,
        sodium: Double
    ): Result<PredictionResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""
            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }

            val predictionRequest = NutritionCalculate.convertToPredictionRequest(
                productName = productName,
                takaranSaji = takaranSaji,
                energyKkal = energyKkal,
                protein = protein,
                fat = fat,
                saturatedFat = saturatedFat,
                sugars = sugars,
                fiber = fiber,
                sodium = sodium
            )

            val authenticatedApiService = APIconfig.getApiService(token)
            val response = authenticatedApiService.predict("Bearer $token", predictionRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}