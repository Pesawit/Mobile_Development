package com.example.pesawit.ui.history

import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.DetectionHistoryItem

class HistoryViewModel : ViewModel() {
    private val historyItems = mutableListOf<DetectionHistoryItem>()

    fun addHistoryItem(item: DetectionHistoryItem) {
        historyItems.add(item)
    }

    fun getHistoryItems(): List<DetectionHistoryItem> {
        return historyItems.toList()
    }
}
