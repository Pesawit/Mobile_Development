package com.example.pesawit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.Data
import com.example.pesawit.data.response.Profile

class ProfileViewModel : ViewModel() {

    private val _userData = MutableLiveData<Profile?>()
    val userData: LiveData<Profile?> = _userData

    // Simulated fetch from server
    fun fetchUserDataFromServer() {
        val dummyData = Data(
            name = "John Doe",
            email = "john.doe@example.com",
            photo = null
        )
        val dummyProfile = Profile(
            data = dummyData,
            message = "User data fetched successfully"
        )
        _userData.value = dummyProfile
    }

    fun saveUserDataToServer(name: String, email: String, onSuccess: () -> Unit, onError: () -> Unit) {
        // Simulate API call to save data to server
        val currentData = _userData.value?.data
        val updatedData = currentData?.copy(
            name = name,
            email = email
        )

        if (updatedData != null) {
            _userData.value = _userData.value?.copy(data = updatedData)
            onSuccess() // Simulate success callback
        } else {
            onError() // Simulate error callback
        }
    }

    fun updateProfileImage(imageUrl: String) {
        val currentData = _userData.value?.data
        val updatedData = currentData?.copy(photo = imageUrl)

        if (updatedData != null) {
            _userData.value = _userData.value?.copy(data = updatedData)
        }
    }

    fun logout() {
        _userData.value = null
    }
}
