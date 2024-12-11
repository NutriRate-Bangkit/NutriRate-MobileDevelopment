package com.example.nutrirateapp.data.model

data class PredictionRequest(
    val productName: String,
    val protein: Double,
    val energy: Double,
    val fat: Double,
    val saturated_fat: Double,
    val sugars: Double,
    val fiber: Double,
    val salt: Double
)

data class PredictionResponse(
    val grade: String
)