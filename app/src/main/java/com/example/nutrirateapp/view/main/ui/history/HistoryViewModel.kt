package com.example.nutrirateapp.view.main.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    // Text for Header
    private val _text = MutableLiveData<String>().apply {
        value = "History"
    }
    val text: LiveData<String> = _text

    // Loading Status
    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading

    // Example: Load Data (simulated)
    fun loadData() {
        _isLoading.value = true
        // Simulate loading data...
        _isLoading.value = false
    }
}
