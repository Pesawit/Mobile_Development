package com.example.pesawit.ui.home.artikel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pesawit.R
import com.example.pesawit.data.response.Article
import com.example.pesawit.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreateArticleFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnUploadImage: Button
    private lateinit var btnCreateArticle: Button
    private lateinit var imageView: ImageView  // ImageView untuk menampilkan gambar yang dipilih

    private val viewModel: HomeViewModel by activityViewModels()

    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.et_title)
        etContent = view.findViewById(R.id.et_content)
        btnUploadImage = view.findViewById(R.id.btn_upload_image)
        btnCreateArticle = view.findViewById(R.id.btn_create_article)
        imageView = view.findViewById(R.id.image_view) // Inisialisasi ImageView

        // Menangani klik tombol upload gambar
        btnUploadImage.setOnClickListener {
            // Memeriksa izin akses penyimpanan
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    IMAGE_PICK_REQUEST_CODE
                )
            } else {
                openImagePicker() // Membuka galeri jika izin sudah diberikan
            }
        }

        // Menangani klik tombol buat artikel
        btnCreateArticle.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Title and content cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageFile == null) {
                Toast.makeText(requireContext(), "Please choose an image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            imageFile?.let {
                viewModel.createArticleWithImage(title, content, it)
                Toast.makeText(requireContext(), "Article created successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createArticleFragment_to_homeFragment)
            }
        }
    }

    // Meminta izin untuk mengakses penyimpanan jika belum diberikan
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IMAGE_PICK_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            openImagePicker() // Memanggil galeri jika izin diberikan
        } else {
            Toast.makeText(requireContext(), "Permission denied to read storage", Toast.LENGTH_SHORT).show()
        }
    }

    // Membuka galeri untuk memilih gambar
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    // Menangani hasil pemilihan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST_CODE) {
            data?.data?.let { uri ->
                // Mengonversi URI ke File
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val file = File(requireContext().cacheDir, "temp_image.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                imageFile = file

                // Menampilkan gambar yang dipilih menggunakan Glide
                Glide.with(requireContext())
                    .load(uri)
                    .into(imageView)  // Menampilkan gambar di ImageView
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 1001
    }
}
