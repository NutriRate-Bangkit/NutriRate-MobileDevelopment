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
    val message: String,
    val user: UpdatedUserData
)

data class UpdatedUserData(
    val name: String,
    val email: String,
    val image: String?
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val message: String
)

data class DeleteAccountResponse(
    val message: String
)

data class ResetPasswordRequest(
    val email: String
)

data class ResetPasswordResponse(
    val message: String,
    val email: String
)