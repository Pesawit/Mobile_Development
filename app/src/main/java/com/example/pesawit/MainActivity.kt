package com.example.pesawit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pesawit.ui.auth.LoginActivity
import com.example.pesawit.ui.home.HomeFragment
import com.example.pesawit.utils.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    interface UserRoleCallback {
        fun onUserRoleReceived(userRole: String?)
    }
    private lateinit var navController: NavController
    private var userRole: String? = null
    private var authToken: String? = null
    private var userRoleCallback: UserRoleCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate executed")
        setContentView(R.layout.activity_main)

        // Ambil userRole dari Intent atau SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userRole = intent.getStringExtra("userRole") ?: sharedPreferences.getString("userRole", null)
        Log.d("MainActivity", "Received userRole: $userRole")

        // Simpan userRole ke SharedPreferences jika belum disimpan
        if (userRole != null) {
            sharedPreferences.edit().putString("userRole", userRole).apply()
        }

        // Cek autentikasi token
        try {
            authToken = TokenManager.getToken(this)
            if (authToken.isNullOrEmpty()) {
                Log.e("MainActivity", "Auth token missing, redirecting to Login")
                redirectToLogin()
                return
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error retrieving token: ${e.message}")
            redirectToLogin()
            return
        }

        // Inisialisasi NavController
        try {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController

            // Cari HomeFragment dan set callback jika ditemukan
            val homeFragment = navHostFragment.childFragmentManager.fragments.firstOrNull { it is HomeFragment } as? HomeFragment
            homeFragment?.let {
                it.onUserRoleReceived(userRole)
            }

            // Inisialisasi BottomNavigationView
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.setupWithNavController(navController)

            // Atur toolbar
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.cameraFragment,
                    R.id.historyFragment,
                    R.id.profileFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)

        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing NavController: ${e.message}")
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    // Implementasi UserRoleCallback
    fun setUserRoleCallback(callback: UserRoleCallback) { // Add this method
        userRoleCallback = callback
    }
}
