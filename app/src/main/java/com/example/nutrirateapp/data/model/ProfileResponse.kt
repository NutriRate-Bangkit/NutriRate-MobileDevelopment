package com.example.nutrirateapp.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("image")
    val image: String?
)

data class UpdateProfileRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("image")
    val image: String?
)

data class UpdateProfileResponse(
    @SerializedName("message")
    val message: String
)