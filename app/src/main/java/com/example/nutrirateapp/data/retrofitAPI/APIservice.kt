package com.example.nutrirateapp.data.retrofitAPI

import com.example.nutrirateapp.data.model.HistoryResponse
import com.example.nutrirateapp.data.model.LoginRequest
import com.example.nutrirateapp.data.model.LoginResponse
import com.example.nutrirateapp.data.model.RegisterRequest
import com.example.nutrirateapp.data.model.RegisterResponse
import com.example.nutrirateapp.data.model.LogoutResponse
import com.example.nutrirateapp.data.model.PredictionRequest
import com.example.nutrirateapp.data.model.PredictionResponse
import com.example.nutrirateapp.data.model.ProfileResponse
import com.example.nutrirateapp.data.model.UpdateProfileRequest
import com.example.nutrirateapp.data.model.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface APIservice {
    @POST("/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/auth/logout")
    suspend fun logout(): LogoutResponse

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse

    @PUT("/profile")
    suspend fun updateProfile( @Header("Authorization") token: String, @Body request: UpdateProfileRequest): UpdateProfileResponse

    @Multipart
    @PUT("/profile") suspend fun updateProfileWithImage(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part): UpdateProfileResponse

    @POST("/predict")
    suspend fun predict( @Header("Authorization") token: String, @Body request: PredictionRequest): PredictionResponse

    @GET("/history")
    suspend fun getHistory(@Header("Authorization") token: String): HistoryResponse
}