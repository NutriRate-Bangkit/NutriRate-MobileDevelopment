package com.example.nutrirateapp.data

data class RegisterRequest(
    val email: String,
    val password: String
)

// RegisterResponse.kt
data class RegisterResponse(
    val message: String,
    val userId:String
)