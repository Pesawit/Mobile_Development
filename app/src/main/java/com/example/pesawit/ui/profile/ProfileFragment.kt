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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfilePicture = view.findViewById(R.id.iv_profile_picture)
        tvUsername = view.findViewById(R.id.tv_username)
        btnEditProfile = view.findViewById(R.id.btn_edit_profile)
        btnLogout = view.findViewById(R.id.btn_logout)

        // Fetch user data from server when fragment is created
        profileViewModel.fetchUserDataFromServer()

        // Observe LiveData for user data
        profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.data?.let { data ->
                Glide.with(requireContext())
                    .load(data.photo ?: R.drawable.default_profile)
                    .placeholder(R.drawable.default_profile)
                    .into(ivProfilePicture)
                tvUsername.text = data.name ?: "Nama tidak tersedia"
            }
        }

        // Edit profile button
        btnEditProfile.setOnClickListener { showEditProfileDialog() }

        // Change profile picture on click
        ivProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // Logout button
        btnLogout.setOnClickListener {
            profileViewModel.logout()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                Glide.with(requireContext()).load(it).into(ivProfilePicture)
                profileViewModel.updateProfileImage(it.toString())
            }
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_profile, null)

        val ivEditProfilePicture = dialogView.findViewById<ImageView>(R.id.iv_edit_profile_picture)
        val btnChangePicture = dialogView.findViewById<Button>(R.id.btn_change_picture)
        val etName = dialogView.findViewById<EditText>(R.id.et_name)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)

        profileViewModel.userData.value?.data?.let { data ->
            Glide.with(requireContext())
                .load(data.photo ?: R.drawable.default_profile)
                .placeholder(R.drawable.default_profile)
                .into(ivEditProfilePicture)
            etName.setText(data.name)
            etEmail.setText(data.email)
        }

        btnChangePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()

                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(requireContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
                } else {
                    profileViewModel.saveUserDataToServer(
                        name,
                        email,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        },
                        onError = {
                            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        dialog.show()
    }
}
