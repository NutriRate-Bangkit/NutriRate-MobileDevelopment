package com.example.nutrirateapp.data.repository

import android.content.Context
import android.net.Uri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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

    suspend fun updateProfile(name: String, email: String, imageUri: Uri?): Result<UpdateProfileResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""
            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }

            val response = if (imageUri != null) {
                // Update dengan gambar
                val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())

                val file = uriToFile(imageUri, context)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                apiService.updateProfileWithImage(
                    "Bearer $token",
                    nameRequestBody,
                    emailRequestBody,
                    imagePart
                )
            } else {
                // Update tanpa gambar
                val request = UpdateProfileRequest(name, email, null)
                apiService.updateProfile("Bearer $token", request)
            }

            Result.success(response)
        } catch (e: Exception) {
            Log.e("UpdateProfile", "Error: ${e.message}")
            Result.failure(e)
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = createTempFile(context)

        contentResolver.openInputStream(uri)?.use { inputStream ->
            myFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return myFile
    }

    private fun createTempFile(context: Context): File {
        val storageDir: File? = context.cacheDir
        return File.createTempFile(
            "TEMP_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
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