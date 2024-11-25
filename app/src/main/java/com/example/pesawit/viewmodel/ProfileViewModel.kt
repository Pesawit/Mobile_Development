package com.example.pesawit.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.ResponseItem

class ProfileViewModel : ViewModel() {

    // MutableLiveData untuk menyimpan data user
    private val _userData = MutableLiveData<ResponseItem>()
    val userData: LiveData<ResponseItem> = _userData

    // Fungsi untuk mengatur data user
    fun setUserData(user: ResponseItem) {
        _userData.value = user
    }

    // Fungsi untuk melakukan logout
    private val _isLoggedOut = MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut

    fun logout() {
        _isLoggedOut.value = true
    }
}