package com.example.pesawit

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.pesawit.utils.TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var authToken: String? = null
    private var userRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate executed")
        setContentView(R.layout.activity_main)

        // Retrieve token and user role
        try {
            authToken = TokenManager.getToken(this)
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            userRole = sharedPreferences.getString("userRole", null)

            if (authToken.isNullOrEmpty() || userRole.isNullOrEmpty()) {
                Log.e("MainActivity", "Auth token or user role missing, redirecting to Login")
                redirectToLogin()
                return
            }

            Log.d("MainActivity", "User Role: $userRole")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error retrieving token or role: ${e.message}")
            redirectToLogin()
            return
        }

        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController

            // Inisialisasi BottomNavigationView
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.setupWithNavController(navController)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)

            // Configure AppBarConfiguration based on user role
            val appBarConfiguration = if (userRole == "admin") {
                // If admin, include all fragments
                AppBarConfiguration(
                    setOf(
                        R.id.homeFragment,
                        R.id.cameraFragment,
                        R.id.historyFragment,
                        R.id.profileFragment
                    )
                )
            } else {
                // If regular user, you might want to restrict certain fragments
                AppBarConfiguration(
                    setOf(
                        R.id.homeFragment,
                        R.id.cameraFragment,
                        R.id.historyFragment,
                        R.id.profileFragment
                    )
                )
            }

            setupActionBarWithNavController(navController, appBarConfiguration)

            // Optionally, you can modify bottom navigation based on user role
            configureBottomNavigation(bottomNav)

            // Hide bottom navigation when CameraFragment is displayed
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.cameraFragment) {
                    bottomNav.visibility = BottomNavigationView.GONE
                } else {
                    bottomNav.visibility = BottomNavigationView.VISIBLE
                }
            }

            Log.d("MainActivity", "NavController successfully initialized")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing NavController: ${e.message}")
            finish()
        }
    }

    private fun configureBottomNavigation(bottomNav: BottomNavigationView) {

        if (userRole == "admin") {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun redirectToLogin() {
        // Clear any stored preferences when logging out
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    // Optional: Add a method to logout
    fun logout() {
        // Clear token
        TokenManager.clearToken(this)

        // Clear user role from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().remove("userRole").apply()

        redirectToLogin()
    }
}
