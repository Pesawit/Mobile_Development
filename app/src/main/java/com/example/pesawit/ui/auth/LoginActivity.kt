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
import com.example.pesawit.utils.ToastUtils
import com.example.pesawit.utils.TokenManager
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                ToastUtils.showToast(this, "Please enter valid credentials")
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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
                        val token = apiResponse?.token
                        val userRole = apiResponse?.user?.role

                        if (!token.isNullOrEmpty() && userRole == "admin") {
                            TokenManager.saveToken(this@LoginActivity, token)
                            navigateToMainActivity()
                        } else {
                            ToastUtils.showToast(this@LoginActivity, "Access Denied: Invalid role")
                        }
                    } else {
                        ToastUtils.showToast(this@LoginActivity, "Login failed: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginError", "Exception: ${e.localizedMessage}")
                    ToastUtils.showToast(this@LoginActivity, "An error occurred during login.")
                }
            }
        }
    }

    private fun handleLoginError(message: String) {
        Log.e("LoginError", "Login failed: $message")
        ToastUtils.showToast(this, "Login failed: $message")
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
