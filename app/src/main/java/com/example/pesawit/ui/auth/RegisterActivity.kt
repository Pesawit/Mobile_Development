package com.example.pesawit.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pesawit.R

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

        // Set click listener untuk tombol register
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (validateInput(username, email, password)) {
                // Lakukan proses registrasi di sini
                // Contoh sederhana:
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke halaman login
            }
        }
    }

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