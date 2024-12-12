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
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pesawit.R
import com.example.pesawit.ui.auth.LoginActivity
import com.example.pesawit.viewmodel.ProfileViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var ivProfilePicture: CircleImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
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
        tvEmail = view.findViewById(R.id.tv_email)
        btnEditProfile = view.findViewById(R.id.btn_edit_profile)
        btnLogout = view.findViewById(R.id.btn_logout)

        // Observe LiveData for user data
        profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                Glide.with(requireContext())
                    .load(it.photo ?: R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(ivProfilePicture)

                tvUsername.text = it.name ?: "Nama tidak tersedia"
                tvEmail.text = it.email ?: "Email tidak tersedia"
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
            // Navigate to login screen
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    @Deprecated("Deprecated in Java")
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

        val ivEditProfilePicture = dialogView.findViewById<CircleImageView>(R.id.iv_edit_profile_picture)
        val btnChangePicture = dialogView.findViewById<Button>(R.id.btn_change_picture)
        val etName = dialogView.findViewById<EditText>(R.id.et_name)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)

        profileViewModel.userData.value?.let { user ->
            Glide.with(requireContext())
                .load(user.photo ?: R.drawable.ic_profile)
                .placeholder(R.drawable.ic_profile)
                .into(ivEditProfilePicture)
            etName.setText(user.name)
            etEmail.setText(user.email)
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