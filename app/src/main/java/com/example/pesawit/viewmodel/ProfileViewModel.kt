package com.example.pesawit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.UserProfile

class ProfileViewModel : ViewModel() {

    private val _userData = MutableLiveData<UserProfile?>()
    val userData: LiveData<UserProfile?> = _userData

    fun saveUserDataToServer(name: String, email: String, onSuccess: () -> Unit, onError: () -> Unit) {
        val currentData = _userData.value
        val updatedData = currentData?.copy(
            name = name,
            email = email
        )

        if (updatedData != null) {
            _userData.value = updatedData
            onSuccess()
        } else {
            onError()
        }
    }

    fun updateProfileImage(imageUrl: String) {
        val currentData = _userData.value
        val updatedData = currentData?.copy(photo = imageUrl)

        if (updatedData != null) {
            _userData.value = updatedData
        }
    }

    fun logout() {
        _userData.value = null
    }
}