package com.example.nutrirateapp.data.repository

import android.content.Context
import android.util.Log
import com.example.nutrirateapp.data.model.HistoryResponse
import com.example.nutrirateapp.data.model.LoginRequest
import com.example.nutrirateapp.data.model.LoginResponse
import com.example.nutrirateapp.data.model.LogoutResponse
import com.example.nutrirateapp.data.model.ProfileResponse
import com.example.nutrirateapp.data.model.RegisterRequest
import com.example.nutrirateapp.data.model.RegisterResponse
import com.example.nutrirateapp.data.model.UpdateProfileRequest
import com.example.nutrirateapp.data.model.UpdateProfileResponse
import com.example.nutrirateapp.data.pref.UserPreferences
import com.example.nutrirateapp.data.retrofitAPI.APIconfig
import kotlinx.coroutines.flow.first

class UserRepository(private val context: Context) {
    private val userPreferences = UserPreferences(context)
    private var apiService = APIconfig.getApiService()

    suspend fun registerUser(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            Log.d("Login", "Token received: ${response.token}")
            userPreferences.saveSession(
                userId = response.userId,
                email = response.email,
                token = response.token
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<LogoutResponse> {
        return try {
            val response = apiService.logout()
            userPreferences.logout()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<ProfileResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""

            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }

            val authenticatedApiService = APIconfig.getApiService(token)
            val response = authenticatedApiService.getProfile("Bearer $token")

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(name: String, email: String, image: String?): Result<UpdateProfileResponse> {
        return try {
            val response = apiService.updateProfile(UpdateProfileRequest(name, email, image))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistory(): Result<HistoryResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""
            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }
            val response = apiService.getHistory("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}