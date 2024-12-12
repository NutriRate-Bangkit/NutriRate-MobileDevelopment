package com.example.nutrirateapp.data.model

data class PredictionRequest(
    val productName: String,
    val protein: Double,
    val energy: Double,
    val fat: Double,
    val saturatedFat: Double,
    val sugars: Double,
    val fiber: Double,
    val sodium: Double,
    val gramPerServing: Double
)

data class PredictionResponse(
    val grade: String,
    val productName: String,
    val originalInputs: OriginalInputs
)

data class OriginalInputs(
    val protein: Double,
    val energy: Double,
    val fat: Double,
    val saturatedFat: Double,
    val sugars: Double,
    val fiber: Double,
    val sodium: Double
)