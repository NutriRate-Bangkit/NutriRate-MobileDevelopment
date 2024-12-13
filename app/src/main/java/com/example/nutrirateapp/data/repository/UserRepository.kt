package com.example.nutrirateapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.nutrirateapp.data.model.ChangePasswordRequest
import com.example.nutrirateapp.data.model.ChangePasswordResponse
import com.example.nutrirateapp.data.model.DeleteAccountResponse
import com.example.nutrirateapp.data.model.HistoryResponse
import com.example.nutrirateapp.data.model.LoginRequest
import com.example.nutrirateapp.data.model.LoginResponse
import com.example.nutrirateapp.data.model.LogoutResponse
import com.example.nutrirateapp.data.model.ProfileResponse
import com.example.nutrirateapp.data.model.RegisterRequest
import com.example.nutrirateapp.data.model.RegisterResponse
import com.example.nutrirateapp.data.model.ResetPasswordRequest
import com.example.nutrirateapp.data.model.ResetPasswordResponse
import com.example.nutrirateapp.data.model.UpdateProfileResponse
import com.example.nutrirateapp.data.pref.UserPreferences
import com.example.nutrirateapp.data.retrofitAPI.APIconfig
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun resetPassword(email: String): Result<ResetPasswordResponse> {
        return try {
            val response = apiService.resetPassword(ResetPasswordRequest(email))
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

    suspend fun updateProfile(
        name: String? = null,
        email: String? = null,
        imageUri: Uri? = null
    ): Result<UpdateProfileResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""
            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }

            // Convert strings to RequestBody
            val nameRequestBody = name?.let {
                RequestBody.create("text/plain".toMediaTypeOrNull(), it)
            }
            val emailRequestBody = email?.let {
                RequestBody.create("text/plain".toMediaTypeOrNull(), it)
            }

            // Handle image upload
            val imagePart = imageUri?.let { uri ->
                // Get file extension from uri
                val extension = when (context.contentResolver.getType(uri)) {
                    "image/jpeg", "image/jpg" -> ".jpg"
                    "image/png" -> ".png"
                    "image/heic" -> ".heic"
                    else -> return Result.failure(Exception("Format file tidak didukung"))
                }

                try {
                    // Create temporary file with correct extension
                    val fileName = "profile_${System.currentTimeMillis()}$extension"
                    val tempFile = File(context.cacheDir, fileName)

                    // Copy uri content to temp file
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }

                    // Create MultipartBody.Part with correct mime type
                    val mimeType = when(extension) {
                        ".jpg", ".jpeg" -> "image/jpeg"
                        ".png" -> "image/png"
                        ".heic" -> "image/heic"
                        else -> "image/jpeg"
                    }

                    val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), tempFile)
                    MultipartBody.Part.createFormData("image", fileName, requestFile)
                } catch (e: Exception) {
                    return Result.failure(Exception("Gagal memproses file gambar"))
                }
            }

            val response = apiService.updateProfile(
                token = "Bearer $token",
                name = nameRequestBody,
                email = emailRequestBody,
                image = imagePart
            )

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<ChangePasswordResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""
            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }

            val response = apiService.changePassword(
                token = "Bearer $token",
                request = ChangePasswordRequest(currentPassword, newPassword)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(): Result<DeleteAccountResponse> {
        return try {
            val token = userPreferences.getToken().first() ?: ""
            if (token.isEmpty()) {
                return Result.failure(Exception("Token tidak tersedia"))
            }

            val response = apiService.deleteAccount("Bearer $token")
            userPreferences.logout()
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