package com.example.pesawit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(R.layout.activity_main)

        // Retrieve user role from Intent (from LoginActivity)
        userRole = intent.getStringExtra("userRole") // Get the user role passed from LoginActivity

        // Retrieve token from TokenManager (assuming token is saved after login)
        authToken = TokenManager.getToken(this)

        // Check if user is authenticated, if not, direct them to login
        if (authToken.isNullOrEmpty()) {
            // Token is missing or invalid, redirect to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Optional: Close MainActivity to prevent back navigation to it
            return
        }

        // Inisialisasi Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment not found")
            return
        }
        navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        // Setup Action Bar with Navigation Controller
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.cameraFragment,
                R.id.historyFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
    }

    override fun onSupportNavigateUp(): Boolean {
        val bundle = Bundle()
        bundle.putString("userRole", userRole) // Pass user role to HomeFragment
        navController.setGraph(R.navigation.nav_graph, bundle)

        // Now you can navigate up as well
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Optionally, implement a function to check token or authenticate on demand
    private fun isAuthenticated(): Boolean {
        return !authToken.isNullOrEmpty()
    }
}
