package com.example.nutrirateapp.view.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutrirateapp.data.model.ChangePasswordResponse
import com.example.nutrirateapp.data.model.DeleteAccountResponse
import com.example.nutrirateapp.data.model.LogoutResponse
import com.example.nutrirateapp.data.model.ProfileResponse
import com.example.nutrirateapp.data.model.UpdateProfileResponse
import com.example.nutrirateapp.data.repository.UserRepository
import com.example.nutrirateapp.view.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _logoutResult = MutableLiveData<Result<LogoutResponse>>()
    val logoutResult: LiveData<Result<LogoutResponse>> = _logoutResult

    private val _profileResult = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> = _profileResult

    private val _updateProfileResult = MutableLiveData<Event<Result<UpdateProfileResponse>>>()
    val updateProfileResult: LiveData<Event<Result<UpdateProfileResponse>>> = _updateProfileResult

    private val _changePasswordResult = MutableLiveData<Event<Result<ChangePasswordResponse>>>()
    val changePasswordResult: LiveData<Event<Result<ChangePasswordResponse>>> = _changePasswordResult

    private val _deleteAccountResult = MutableLiveData<Event<Result<DeleteAccountResponse>>>()
    val deleteAccountResult: LiveData<Event<Result<DeleteAccountResponse>>> = _deleteAccountResult

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

    fun updateProfile(name: String? = null, email: String? = null, imageUri: Uri? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.updateProfile(name, email, imageUri)
                // Wrap result dalam Event
                _updateProfileResult.value = Event(result)
                if (result.isSuccess) {
                    getProfile() // Refresh profile data
                }
            } catch (e: Exception) {
                _updateProfileResult.value = Event(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.changePassword(currentPassword, newPassword)
                _changePasswordResult.value = Event(result)
            } catch (e: Exception) {
                _changePasswordResult.value = Event(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.deleteAccount()
                _deleteAccountResult.value = Event(result)
            } catch (e: Exception) {
                _deleteAccountResult.value = Event(Result.failure(e))
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