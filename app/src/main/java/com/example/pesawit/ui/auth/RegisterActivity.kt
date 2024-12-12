package com.example.pesawit.ui.auth

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pesawit.R
import com.example.pesawit.data.response.Register
import com.example.pesawit.data.response.ResponseAPI
import com.example.pesawit.data.retrofit.ApiConfig
import com.example.pesawit.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etusername)
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.et_ulangpassword)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInput(username, email, password, confirmPassword)) {
                registerUser(username, email, password, confirmPassword)
            } else {
                ToastUtils.showToast(this, "Please fill all fields correctly!")
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return username.isNotEmpty() &&
                email.isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty() &&
                password == confirmPassword
    }

    private fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        lifecycleScope.launch {
            try {
                val requestBody = mapOf(
                    "name" to username,
                    "email" to email,
                    "password" to password,
                    "confirmPassword" to confirmPassword // Sesuai dengan API
                )
                Log.d("RegisterRequest", "Request body: $requestBody")

                val response = ApiConfig.provideApiService(applicationContext).registerUser(requestBody)

                withContext(Dispatchers.Main) {
                    if (!isFinishing) {
                        if (response.isSuccessful) {
                            val registerResponse: ResponseAPI<Register>? = response.body()
                            if (registerResponse?.success == true) {
                                ToastUtils.showToast(this@RegisterActivity, "Registration successful!")
                                finish()
                            } else {
                                val message = registerResponse?.message ?: "Unknown error"
                                ToastUtils.showToast(this@RegisterActivity, "Registration failed: $message")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("RegisterError", "HTTP Error: ${response.code()} - $errorBody")
                            ToastUtils.showToast(this@RegisterActivity, "Registration failed: ${errorBody ?: response.message()}")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (!isFinishing) {
                        Log.e("RegisterError", "Exception: ${e.localizedMessage}")
                        ToastUtils.showToast(this@RegisterActivity, "An error occurred during registration.")
                    }
                }
            }
        }
    }
}
