package com.example.nutrirateapp.data.model

data class HistoryResponse(
    val history: List<HistoryItem>
)

data class HistoryItem(
    val id: String,
    val userId: String,
    val name: String,
    val originalInputs: OriginalInputs,
    val productName: String,
    val gramPerServing: Double,
    val grade: String,
    val timestamp: Any
)