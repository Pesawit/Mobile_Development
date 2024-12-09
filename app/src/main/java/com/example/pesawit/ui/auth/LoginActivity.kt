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
import com.example.pesawit.ui.home.AdminAdapter
import com.example.pesawit.utils.ToastHelper
import com.example.pesawit.utils.TokenManager
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        // Google Sign-In setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id)) // Pastikan Client ID sudah benar
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

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

        // Google Sign-In Button Click Handler
        findViewById<Button>(R.id.btn_google_sign_in).setOnClickListener {
            signInWithGoogle()
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

                                // Penempatan if untuk menangani peran pengguna
                                if (userRole == "admin") {
                                    // Arahkan ke tampilan admin
                                    val intent = Intent(this@LoginActivity, AdminAdapter::class.java)
                                    startActivity(intent)
                                } else {
                                    // Arahkan ke tampilan user biasa
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

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
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
        } catch (e: Exception) {
            Log.e("AuthError", "Error retrieving token", e)
        }
    }
}
