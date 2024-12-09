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

        profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.data?.let { data ->
                Glide.with(requireContext())
                    .load(data.photo ?: R.drawable.default_profile)
                    .placeholder(R.drawable.default_profile)
                    .into(ivProfilePicture)
                tvUsername.text = data.name ?: "Nama tidak tersedia"
            }
        }

        btnEditProfile.setOnClickListener { showEditProfileDialog() }

        ivProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        btnLogout.setOnClickListener { profileViewModel.logout() }
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
        val etName = dialogView.findViewById<EditText>(R.id.et_name)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)

        profileViewModel.userData.value?.data?.let { data ->
            etName.setText(data.name)
            etEmail.setText(data.email)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                profileViewModel.updateUserData(
                    etName.text.toString(),
                    etEmail.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
