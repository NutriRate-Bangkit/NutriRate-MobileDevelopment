package com.example.nutrirateapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutrirateapp.data.model.HistoryItem

class DetailHistoryViewModel : ViewModel() {
    private val _historyDetail = MutableLiveData<HistoryItem>()
    val historyDetail: LiveData<HistoryItem> = _historyDetail

    fun setHistoryDetail(historyItem: HistoryItem) {
        _historyDetail.value = historyItem
    }
}