package com.example.pesawit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.History

class HistoryViewModel : ViewModel() {
    private val historyItems = mutableListOf<History>()

    fun addHistoryItem(item: History) {
        historyItems.add(item)
    }

    fun getHistoryItems(): List<History> {
        return historyItems.toList()
    }
}
