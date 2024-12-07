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
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.utils.ToastHelper
import com.example.pesawit.utils.TokenManager
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var mGoogleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi views
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        // Google Sign-In setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id)) // Pastikan Client ID sudah benar
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (validateInput(email, password)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = getUserRole(email, password)
                    runOnUiThread {
                        if (user != null) {
                            ToastHelper.showToast(this@LoginActivity, "Welcome, ${user.name}")
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            ToastHelper.showToast(this@LoginActivity, "Invalid email or password")
                        }
                    }
                }
            } else {
                ToastHelper.showToast(this@LoginActivity, "Please fill all fields correctly")
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Google Sign-In Button Click Handler
        findViewById<Button>(R.id.btn_google_sign_in).setOnClickListener {
            signInWithGoogle()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity", "onDestroy called")
    }

    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Fungsi untuk mendapatkan role pengguna dan menyimpan token
    private suspend fun getUserRole(email: String, password: String): ResponseItem? {
        return try {
            val requestBody = mapOf("email" to email, "password" to password)
            val response = ApiConfig.provideApiService(applicationContext).loginUser(requestBody)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data?.let {
                        val token = it.token // Pastikan token diterima
                        if (!token.isNullOrEmpty()) {
                            TokenManager.saveToken(this@LoginActivity, token)
                        }
                    }
                    apiResponse.data
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

    // Fungsi untuk menangani login menggunakan Google
    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Menangani hasil dari Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    // Mendapatkan token setelah login sukses
                    getAuthToken(account)
                }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign-in failed", e)
            }
        }
    }

    private fun getAuthToken(account: GoogleSignInAccount) {
        val scope = "oauth2:https://www.googleapis.com/auth/googleplay"
        try {
            val token = account.email?.let { GoogleAuthUtil.getToken(this, it, scope) }
            Log.d("AuthToken", "Token: $token")
            // Anda dapat menggunakan token untuk autentikasi API
        } catch (e: Exception) {
            Log.e("AuthError", "Error retrieving token", e)
        }
    }
}
