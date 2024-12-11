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
    private var userRole: String? = null
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("com.example.pesawit.MainActivity", "onCreate executed")
        setContentView(R.layout.activity_main)

        // Ambil userRole dari Intent
        userRole = intent.getStringExtra("userRole")

        // Jika userRole null, coba ambil dari SharedPreferences
        if (userRole.isNullOrEmpty()) {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            userRole = sharedPreferences.getString("userRole", null)
            Log.d("com.example.pesawit.MainActivity", "Retrieved userRole from SharedPreferences: $userRole")
        }

        // Cek autentikasi token
        try {
            authToken = TokenManager.getToken(this)
            if (authToken.isNullOrEmpty()) {
                Log.e("com.example.pesawit.MainActivity", "Auth token missing, redirecting to Login")
                redirectToLogin()
                return
            }
        } catch (e: Exception) {
            Log.e("com.example.pesawit.MainActivity", "Error retrieving token: ${e.message}")
            redirectToLogin()
            return
        }

        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController

            // Kirim userRole ke HomeFragment melalui arguments
            val bundle = Bundle().apply { putString("userRole", userRole) }
            navController.setGraph(R.navigation.nav_graph, bundle)

            // Inisialisasi BottomNavigationView
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.setupWithNavController(navController)

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

            Log.d("com.example.pesawit.MainActivity", "Successfully passed userRole: $userRole")
        } catch (e: Exception) {
            Log.e("com.example.pesawit.MainActivity", "Error initializing NavController: ${e.message}")
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
}
