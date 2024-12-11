package com.example.pesawit.ui.auth

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.pesawit.R
import com.example.pesawit.data.retrofit.ApiConfig
import com.example.pesawit.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var radioGroupRole: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etusername)
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.et_ulangpassword)
        btnRegister = findViewById(R.id.btn_register)
        radioGroupRole = findViewById(R.id.rg_roles)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            val selectedRoleId = radioGroupRole.checkedRadioButtonId
            if (selectedRoleId == -1 || password != confirmPassword) {
                ToastUtils.showToast(this, "Invalid inputs or passwords do not match")
                return@setOnClickListener
            }

            val selectedRole = findViewById<RadioButton>(selectedRoleId).text.toString()
            registerUser(username, email, password, selectedRole)
        }
    }

    private fun registerUser(username: String, email: String, password: String, role: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val requestBody = mapOf(
                    "name" to username,
                    "email" to email,
                    "password" to password,
                    "role" to role
                )
                val response = ApiConfig.provideApiService(applicationContext).registerUser(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        ToastUtils.showToast(this@RegisterActivity, "Registration successful!")
                        finish()
                    } else {
                        ToastUtils.showToast(this@RegisterActivity, "Registration failed: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RegisterError", e.localizedMessage ?: "Unknown error")
                    ToastUtils.showToast(this@RegisterActivity, "An error occurred during registration.")
                }
            }
        }
    }
}