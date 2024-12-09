package com.example.nutrirateapp.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val userId: String,
    val email: String
)