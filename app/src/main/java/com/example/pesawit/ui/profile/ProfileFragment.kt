package com.example.pesawit.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.pesawit.R
import com.example.pesawit.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button
    private val profileViewModel: ProfileViewModel by activityViewModels()

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }

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

        // Observasi data user
        profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                Glide.with(requireContext())
                    .load(it.image ?: R.drawable.default_profile)
                    .placeholder(R.drawable.default_profile)
                    .into(ivProfilePicture)
                tvUsername.text = it.name ?: "Nama tidak tersedia"
            }
        }

        // Tombol Edit Profil
        btnEditProfile.setOnClickListener {
            showEditProfileDialog() // Tampilkan dialog untuk mengedit profil
        }

        // Unggah Foto Profil
        ivProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            profileViewModel.logout()
        }
    }

    // Fungsi untuk menangani hasil unggahan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                Glide.with(requireContext())
                    .load(it)
                    .into(ivProfilePicture)
                profileViewModel.updateProfileImage(it.toString()) // Update gambar profil di ViewModel
            }
        }
    }

    // Fungsi untuk menampilkan dialog edit profil
    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val etName = dialogView.findViewById<EditText>(R.id.et_name)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)
        val etPhone = dialogView.findViewById<EditText>(R.id.et_phone)

        // Setel data lama di dialog
        profileViewModel.userData.value?.let { user ->
            etName.setText(user.name)
            etEmail.setText(user.email)
            etPhone.setText(user.noTelp)
        }

        // Tampilkan dialog untuk edit profil
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val phone = etPhone.text.toString()

                // Update data user di ViewModel
                profileViewModel.updateUserData(name, email, phone)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
