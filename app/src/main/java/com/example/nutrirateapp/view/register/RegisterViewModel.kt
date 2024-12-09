package com.example.nutrirateapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrirateapp.data.model.RegisterResponse
import com.example.nutrirateapp.data.repository.UserRepository
import kotlinx.coroutines.launch


class RegisterViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("Register", "Attempting to register user: $email")
                val result = repository.registerUser(name, email, password)
                Log.d("Register", "Registration result: $result")
                _registerResult.value = result
            } catch (e: Exception) {
                Log.e("Register", "Registration error", e)
                _registerResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}