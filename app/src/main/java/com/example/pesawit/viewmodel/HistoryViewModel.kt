package com.example.pesawit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.ResponseItem

class HistoryViewModel : ViewModel() {
    private val historyItems = mutableListOf<ResponseItem>()

    fun addHistoryItem(item: ResponseItem) {
        historyItems.add(item)
    }

    fun getHistoryItems(): List<ResponseItem> {
        return historyItems.toList()
    }
}