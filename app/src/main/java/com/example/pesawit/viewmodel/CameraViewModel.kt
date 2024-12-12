package com.example.pesawit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraViewModel : ViewModel() {

    @Suppress("unused")
    fun createImageFile(context: Context): File {
        // Membuat nama file unik berdasarkan timestamp
        val timeStamp: String = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())

        // Direktori penyimpanan di cache aplikasi
        val storageDir: File? = context.cacheDir

        // Membuat file gambar
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* Prefix nama file */
            ".jpg", /* Suffix (format file) */
            storageDir /* Direktori penyimpanan */
        )
    }
}
