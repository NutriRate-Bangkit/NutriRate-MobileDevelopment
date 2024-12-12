package com.example.nutrirateapp.data.nutritioncalculate

import com.example.nutrirateapp.data.model.PredictionRequest

class NutritionCalculate {
    companion object {
        private fun perhitunganNutrisi(nilai: Double, takaranSaji: Double): Double {
            return (nilai * 100) / takaranSaji
        }

        private fun perhitunganEnergyKj(nilaiKkal: Double, takaranSaji: Double): Double {
            val nilaiKj = nilaiKkal * 4.18
            return (nilaiKj * 100) / takaranSaji
        }

        private fun perhitunganSalt(nilaiSodium: Double, takaranSaji: Double): Double {
            val nilaiSalt = (nilaiSodium * 2.5) / 1000
            return (nilaiSalt * 100) / takaranSaji
        }

        fun convertToPredictionRequest(
            productName: String,
            takaranSaji: Double,
            energyKkal: Double,
            protein: Double,
            fat: Double,
            saturatedFat: Double,
            sugars: Double,
            fiber: Double,
            sodium: Double
        ): PredictionRequest {
            return PredictionRequest(
                productName = productName,
                energy = perhitunganEnergyKj(energyKkal, takaranSaji),
                protein = perhitunganNutrisi(protein, takaranSaji),
                fat = perhitunganNutrisi(fat, takaranSaji),
                saturated_fat = perhitunganNutrisi(saturatedFat, takaranSaji),
                sugars = perhitunganNutrisi(sugars, takaranSaji),
                fiber = perhitunganNutrisi(fiber, takaranSaji),
                salt = perhitunganSalt(sodium, takaranSaji)
            )
        }
    }
}