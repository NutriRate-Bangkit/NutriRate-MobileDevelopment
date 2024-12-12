package com.example.nutrirateapp.view.main.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutrirateapp.data.model.HistoryResponse
import com.example.nutrirateapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _historyResult = MutableLiveData<Result<HistoryResponse>>()
    val historyResult: LiveData<Result<HistoryResponse>> = _historyResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getHistory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.getHistory()
                _historyResult.value = result
            } catch (e: Exception) {
                _historyResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}