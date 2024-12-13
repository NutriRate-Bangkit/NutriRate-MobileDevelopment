package com.example.nutrirateapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class HistoryResponse(
    val history: List<HistoryItem>
)

@Parcelize
data class HistoryItem(
    val id: String,
    val userId: String,
    val name: String,
    val originalInputs: OriginalInputs,
    val productName: String,
    val gramPerServing: Double,
    val grade: String,
) : Parcelable