package com.example.nutrirateapp.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutrirateapp.data.model.LoginResponse
import com.example.nutrirateapp.data.model.ResetPasswordResponse
import com.example.nutrirateapp.data.repository.UserRepository
import com.example.nutrirateapp.view.utils.Event
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _resetPasswordResult = MutableLiveData<Event<Result<ResetPasswordResponse>>>()
    val resetPasswordResult: LiveData<Event<Result<ResetPasswordResponse>>> = _resetPasswordResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.loginUser(email, password)
                _loginResult.value = result
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.resetPassword(email)
                _resetPasswordResult.value = Event(result)
            } catch (e: Exception) {
                _resetPasswordResult.value = Event(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}