package com.example.pesawit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraViewModel : ViewModel() {

    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
        val storageDir = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    fun handleCapturedImage(imageFile: File) {
        // TODO: Implement image handling logic
        // For example, you might want to:
        // - Upload the image to a server
        // - Save image details to a database
        // - Trigger navigation to another fragment
    }

    fun navigateToResultScreen() {
        // Trigger navigation to the "Hasil Prediksi" screen
    }
}