package com.example.nutrirateapp.data.model

data class ProfileResponse(
    val name: String,
    val email: String,
    val image: String?
)

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val image: String?
)

data class UpdateProfileResponse(
    val message: String
)