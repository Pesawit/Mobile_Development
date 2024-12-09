package com.example.pesawit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pesawit.R
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfilePicture = view.findViewById(R.id.iv_profile_picture)
        tvUsername = view.findViewById(R.id.tv_username)
        btnEditProfile = view.findViewById(R.id.btn_edit_profile)
        btnLogout = view.findViewById(R.id.btn_logout)

        // Observasi perubahan data user
        profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Setel gambar profil menggunakan Glide
                Glide.with(requireContext())
                    .load(it.image)  // Pastikan image URL valid atau gunakan gambar default jika null
                    .placeholder(R.drawable.default_profile)  // Placeholder jika URL null
                    .into(ivProfilePicture)

                // Setel nama pengguna
                tvUsername.text = it.name ?: "Nama tidak tersedia"
            }
        }

        // Observasi logout
        profileViewModel.isLoggedOut.observe(viewLifecycleOwner) { isLoggedOut ->
            if (isLoggedOut) {
                // Tindakan setelah logout, misalnya kembali ke layar login
                requireActivity().finish()
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
