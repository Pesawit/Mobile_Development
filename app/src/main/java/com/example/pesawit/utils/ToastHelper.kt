package com.example.pesawit.utils


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastHelper {
    private var isToastVisible = false

    fun showToast(context: Context, message: String) {
        if (!isToastVisible) {
            isToastVisible = true
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ isToastVisible = false }, 2000)
        }
    }
}
