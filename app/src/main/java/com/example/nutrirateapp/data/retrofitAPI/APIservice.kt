package com.example.nutrirateapp.data.retrofitAPI

import com.example.nutrirateapp.data.model.ChangePasswordRequest
import com.example.nutrirateapp.data.model.ChangePasswordResponse
import com.example.nutrirateapp.data.model.DeleteAccountResponse
import com.example.nutrirateapp.data.model.HistoryResponse
import com.example.nutrirateapp.data.model.LoginRequest
import com.example.nutrirateapp.data.model.LoginResponse
import com.example.nutrirateapp.data.model.RegisterRequest
import com.example.nutrirateapp.data.model.RegisterResponse
import com.example.nutrirateapp.data.model.LogoutResponse
import com.example.nutrirateapp.data.model.PredictionRequest
import com.example.nutrirateapp.data.model.PredictionResponse
import com.example.nutrirateapp.data.model.ProfileResponse
import com.example.nutrirateapp.data.model.ResetPasswordRequest
import com.example.nutrirateapp.data.model.ResetPasswordResponse
import com.example.nutrirateapp.data.model.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

    @POST("/auth/logout")
    suspend fun logout(): LogoutResponse

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse

    @Multipart
    @PUT("/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part image: MultipartBody.Part?
    ): UpdateProfileResponse

    @PUT("/profile")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): ChangePasswordResponse

    @DELETE("/profile")
    suspend fun deleteAccount(
        @Header("Authorization") token: String
    ): DeleteAccountResponse

    @POST("/predict")
    suspend fun predict( @Header("Authorization") token: String, @Body request: PredictionRequest): PredictionResponse

    @GET("/history")
    suspend fun getHistory(@Header("Authorization") token: String): HistoryResponse
}