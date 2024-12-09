package com.example.pesawit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.Profile

class ProfileViewModel : ViewModel() {

    private val _userData = MutableLiveData<Profile>()
    val userData: LiveData<Profile> = _userData

    fun setUserData(user: Profile) {
        _userData.value = user
    }

    fun updateProfileImage(imageUrl: String) {
        _userData.value = _userData.value?.copy(
            data = _userData.value?.data?.copy(photo = imageUrl)
        )
    }


    fun updateUserData(name: String?, email: String?) {
        _userData.value = _userData.value?.copy(
            data = _userData.value?.data?.copy(
                name = name,
                email = email
            )
        )
    }

    private val _isLoggedOut = MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut

    fun logout() {
        _isLoggedOut.value = true
    }
}
