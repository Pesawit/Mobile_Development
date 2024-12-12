package com.example.pesawit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.DetectionHistory

class HistoryViewModel : ViewModel() {
    private val historyItems = mutableListOf<DetectionHistory>()

    fun addHistoryItem(item: DetectionHistory) {
        historyItems.add(item)
    }

    fun getHistoryItems(): List<DetectionHistory> {
        return historyItems.toList()
    }
}
