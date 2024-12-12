package com.example.pesawit.ui.camera


import okhttp3.MultipartBody
import java.io.File
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody


// Extension function for Uri to convert to MultipartBody.Part
fun Uri.toMultipartBodyPart(): MultipartBody.Part? {
    val filePath = this.path ?: return null
    val file = File(filePath)

    // Ensure the file exists and is not empty
    if (!file.exists() || file.length() == 0L) return null

    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("image", file.name, requestBody)
}

