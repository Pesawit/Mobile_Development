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
import org.json.JSONObject
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi views
        etUsername = findViewById(R.id.etusername)
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.et_ulangpassword)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            if (password != confirmPassword) {
                etPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            if (validateInput(username, email, password)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val requestBody = mapOf(
                        "name" to username,
                        "email" to email,
                        "password" to password,
                        "confirmPassword" to confirmPassword
                    )
                    Log.d("RegisterRequest", "Request body: $requestBody")

                    // Panggil ApiService dengan konteks aplikasi
                    val response = ApiConfig.provideApiService(applicationContext).registerUser(requestBody)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse?.message == "User registered successfully") {
                                Log.d("RegisterSuccess", "Server message: ${apiResponse.message}")
                                ToastHelper.showToast(this@RegisterActivity, "Registration successful!")
                                finish() // Close activity on success
                            } else {
                                Log.e("RegisterError", "Server message: ${apiResponse?.message}")
                                ToastHelper.showToast(this@RegisterActivity, "Registration failed: ${apiResponse?.message}")
                            }
                        } else {
                            handleError(response)
                        }
                    }
                }
            }
        }
    }

    private fun handleError(response: Response<*>) {
        Log.e("RegisterError", "HTTP error: ${response.code()} - ${response.message()}")

        response.errorBody()?.let { errorBody ->
            try {
                val errorString = errorBody.string()
                Log.e("RegisterError", "Error response body: $errorString")

                val jsonError = JSONObject(errorString)
                val errorDetail = jsonError.optString("detail", "No detail available")
                Log.e("RegisterError", "Error detail: $errorDetail")
                ToastHelper.showToast(this@RegisterActivity, "Registration failed: $errorDetail")
            } catch (e: Exception) {
                Log.e("RegisterError", "Error parsing error body: ${e.localizedMessage}")
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