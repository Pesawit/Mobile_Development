package com.example.pesawit.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pesawit.MainActivity
import com.example.pesawit.R
import com.example.pesawit.data.retrofit.ApiConfig
import com.example.pesawit.utils.ToastUtils
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
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()
    }

    private fun login(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val loginRequest = mapOf("email" to email, "password" to password)
                val response = ApiConfig.provideApiService(applicationContext).loginUser(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()

                        if (loginResponse != null) {
                            val token = loginResponse.token
                            val role = loginResponse.role

                            if (token != null && role != null) {
                                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                sharedPreferences.edit().apply {
                                    putString("authToken", token)
                                    putString("userRole", role)
                                    apply()
                                }
                                navigateToMainActivity()
                            } else {
                                ToastUtils.showToast(this@LoginActivity, "Login failed: Token or Role is missing.")
                            }
                        } else {
                            ToastUtils.showToast(this@LoginActivity, "Login failed: Response is empty.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("LoginDebug", "Error Body: $errorBody")
                        ToastUtils.showToast(this@LoginActivity, "Login failed: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginDebug", "Exception: ${e.localizedMessage}")
                    ToastUtils.showToast(this@LoginActivity, "An error occurred during login.")
                }
            }
        }
    }
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
