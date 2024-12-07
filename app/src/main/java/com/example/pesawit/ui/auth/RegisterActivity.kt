package com.example.pesawit.ui.auth

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.pesawit.R
import com.example.pesawit.data.retrofit.ApiConfig
import com.example.pesawit.utils.ToastHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi views
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (validateInput(username, email, password)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val requestBody = mapOf(
                        "username" to username,
                        "email" to email,
                        "password" to password
                    )
                    // Panggil ApiService dengan konteks aplikasi
                    val response = ApiConfig.provideApiService(applicationContext).registerUser(requestBody)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse?.success == true) {
                                ToastHelper.showToast(this@RegisterActivity, "Registration successful!")
                                finish() // Menutup activity jika registrasi sukses
                            } else {
                                Log.e("RegisterError", "Server message: ${apiResponse?.message}")
                                ToastHelper.showToast(this@RegisterActivity, "Registration failed: ${apiResponse?.message}")
                            }
                        } else {
                            Log.e("RegisterError", "HTTP error: ${response.code()} - ${response.message()}")
                            ToastHelper.showToast(this@RegisterActivity, "Registration failed: ${response.message()}")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("RegisterActivity", "onDestroy called")
    }

    // Fungsi validasi input
    private fun validateInput(username: String, email: String, password: String): Boolean {
        if (username.isEmpty()) {
            etUsername.error = "Username cannot be empty"
            return false
        }
        if (email.isEmpty()) {
            etEmail.error = "Email cannot be empty"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Invalid email format"
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "Password cannot be empty"
            return false
        }
        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }
}
