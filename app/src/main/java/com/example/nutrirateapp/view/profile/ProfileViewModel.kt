package com.example.nutrirateapp.view.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutrirateapp.data.model.LogoutResponse
import com.example.nutrirateapp.data.model.ProfileResponse
import com.example.nutrirateapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _logoutResult = MutableLiveData<Result<LogoutResponse>>()
    val logoutResult: LiveData<Result<LogoutResponse>> = _logoutResult

    private val _profileResult = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> = _profileResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.getProfile()
                _profileResult.value = result
            } catch (e: Exception) {
                _profileResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.logout()
                _logoutResult.value = result
            } catch (e: Exception) {
                _logoutResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}