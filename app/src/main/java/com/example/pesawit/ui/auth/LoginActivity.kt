package com.example.pesawit.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pesawit.MainActivity
import com.example.pesawit.R
import com.example.pesawit.data.retrofit.ApiConfig
import com.example.pesawit.utils.ToastHelper
import com.example.pesawit.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi views
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                login(email, password)
            } else {
                ToastHelper.showToast(this, "Please enter valid credentials")
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun login(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val requestBody = mapOf("email" to email, "password" to password)
                val response = ApiConfig.provideApiService(applicationContext).loginUser(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.token != null) {
                            val token = apiResponse.token
                            val userRole = apiResponse.role
                            if (token.isNotEmpty()) {
                                TokenManager.saveToken(this@LoginActivity, token)

                                if (userRole == "admin") {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java) // Ganti dengan tampilan admin jika ada
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                        putExtra("userRole", userRole)
                                    }
                                    startActivity(intent)
                                }

                                finish()
                            } else {
                                ToastHelper.showToast(this@LoginActivity, "Login failed: Token not found")
                            }
                        } else {
                            ToastHelper.showToast(this@LoginActivity, "Login failed: Invalid response data")
                        }
                    } else {
                        Log.e("LoginError", "Response error: ${response.message()}")
                        ToastHelper.showToast(this@LoginActivity, "Login failed: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginError", "Exception occurred: ${e.localizedMessage}")
                    ToastHelper.showToast(this@LoginActivity, "An error occurred during login.")
                }
            }
        }
    }
}
