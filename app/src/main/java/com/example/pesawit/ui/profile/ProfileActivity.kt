package com.example.pesawit.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pesawit.R
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button
    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ivProfilePicture = findViewById(R.id.iv_profile_picture)
        tvUsername = findViewById(R.id.tv_username)
        btnEditProfile = findViewById(R.id.btn_edit_profile)
        btnLogout = findViewById(R.id.btn_logout)

        // Observasi perubahan data user
        profileViewModel.userData.observe(this) { user ->
            user?.let {
                // Setel gambar profil menggunakan Glide
                Glide.with(this)
                    .load(it.image)  // Pastikan image URL valid atau gunakan gambar default jika null
                    .placeholder(R.drawable.default_profile)  // Placeholder jika URL null
                    .into(ivProfilePicture)

                // Setel nama pengguna
                tvUsername.text = it.name ?: "Nama tidak tersedia"
            }
        }

        // Observasi logout
        profileViewModel.isLoggedOut.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                // Tindakan setelah logout, misalnya kembali ke layar login
                finish()
            }
        }

        // Data contoh user (bisa dari intent atau API)
        val user = ResponseItem(
            image = "https://example.com/profile.jpg", // URL gambar profil
            name = "John Doe"
        )
        profileViewModel.setUserData(user)

        // Tombol Edit Profil
        btnEditProfile.setOnClickListener {
            // Tindakan ketika tombol Edit Profile diklik
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            profileViewModel.logout() // Trigger logout state
        }
    }
}

private fun getUserData(): ResponseItem {
    return ResponseItem(
        image = "https://example.com/profile.jpg",
        name = "John Doe"
    )
}

