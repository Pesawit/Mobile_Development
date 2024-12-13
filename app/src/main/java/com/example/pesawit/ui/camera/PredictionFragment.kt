package com.example.pesawit.ui.camera

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pesawit.databinding.FragmentPredictionBinding
import com.example.pesawit.viewmodel.PredictionViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PredictionFragment : Fragment() {

    private lateinit var binding: FragmentPredictionBinding
    private val viewModel: PredictionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the arguments passed to this fragment
        val args = PredictionFragmentArgs.fromBundle(requireArguments())
        val imageUri = Uri.parse(args.imageUri)

        // Display the selected image
        binding.ivPredictedImage.setImageURI(imageUri)

        // Call the API to upload the image and get the prediction
        val imagePart = createImagePart(imageUri)
        viewModel.uploadImage(imagePart)

        // Observe the result and update the UI
        viewModel.predictionResult.observe(viewLifecycleOwner) { result ->
            if (result?.data != null) {
                binding.tvPredictedResult.text = "Hasil Prediksi: ${result.data.result ?: "Tidak tersedia"}"
                binding.tvRecommendation.text = "Rekomendasi: ${result.data.recommendation ?: "Tidak ada rekomendasi"}"
            } else {
                binding.tvPredictedResult.text = "Gagal memuat hasil prediksi"
                binding.tvRecommendation.text = "Tidak ada rekomendasi"
            }
        }




        // Observe loading state and show/hide ProgressBar
        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    // Create the image part for the upload
    private fun createImagePart(uri: Uri): MultipartBody.Part {
        // Get the file from the Uri
        val file = File(uri.path!!)  // Ensure the Uri path points to a valid file
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())  // Create request body with MIME type

        // Return the MultipartBody.Part for the image
        return MultipartBody.Part.createFormData("image", file.name, requestBody)  // Create MultipartBody.Part
    }
}
