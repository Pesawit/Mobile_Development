package com.example.pesawit.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pesawit.data.response.DetectionHistory
import com.example.pesawit.data.retrofit.ApiClient
import com.example.pesawit.data.retrofit.ApiService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class PredictionViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData for prediction result (nullable)
    private val _predictionResult = MutableLiveData<DetectionHistory?>()
    val predictionResult: LiveData<DetectionHistory?> get() = _predictionResult

    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // API service instance
    private val apiService: ApiService = ApiClient.getClient().create(ApiService::class.java)

    // Function to upload the image and get the prediction
    fun uploadImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            // Start loading
            _isLoading.value = true

            try {
                // Perform the API call
                val response = apiService.uploadImage(image,
                    "someParam".toRequestBody("text/plain".toMediaTypeOrNull())
                )

                // Stop loading
                _isLoading.value = false

                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        Log.d("API Response", result.toString())

                        _predictionResult.value = result
                    } else {
                        Log.e("PredictionViewModel", "Response body is null")
                        _predictionResult.value = null
                    }
                } else {
                    Log.e("PredictionViewModel", "API Error: ${response.code()} - ${response.message()}")
                    _predictionResult.value = null
                }

            } catch (e: Exception) {
                // Stop loading
                _isLoading.value = false
                // Handle exception
                _predictionResult.value = null
            }
        }
    }
}
