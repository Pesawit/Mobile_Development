package com.example.pesawit.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pesawit.MainActivity
import com.example.pesawit.R
import com.example.pesawit.data.retrofit.ApiConfig
import com.example.pesawit.data.response.ResponseItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi view
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        // Set listener untuk tombol login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (validateInput(email, password)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = getUserRole(email, password)
                    runOnUiThread {
                        if (user != null) {
                            Toast.makeText(this@LoginActivity, "Welcome, ${user.name}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // Set listener untuk teks register
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity", "onDestroy called")
    }

    // Validasi input login
    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private suspend fun getUserRole(email: String, password: String): ResponseItem? {
        return try {
            val requestBody = mapOf("email" to email, "password" to password)
            val response = ApiConfig.apiService.loginUser(requestBody)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data // Mengembalikan objek ResponseItem
                } else {
                    Log.e("LoginError", "Error message: ${apiResponse?.message}")
                    null
                }
            } else {
                Log.e("LoginError", "Response failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("LoginError", "Exception occurred", e)
            null
        }
    }

}
