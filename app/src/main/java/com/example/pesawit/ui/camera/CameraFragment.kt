package com.example.pesawit.ui.camera

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pesawit.data.response.ApiResponse
import com.example.pesawit.data.response.DetectionHistoryItem
import com.example.pesawit.databinding.FragmentCameraBinding
import com.example.pesawit.data.retrofit.ApiClient
import com.example.pesawit.viewmodel.CameraViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private val viewModel: CameraViewModel by viewModels()
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions and start camera
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 0)
        }

        binding.btnCapture.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = viewModel.createImageFile(requireContext())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Photo capture failed: ${exc.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    binding.ivUploadedImage.visibility = View.VISIBLE
                    binding.ivUploadedImage.setImageURI(Uri.fromFile(photoFile))

                    binding.viewFinder.visibility = View.GONE
                    binding.llActionButtons.visibility = View.VISIBLE

                    uploadImageToServer(photoFile)
                }
            }
        )
    }

    private fun uploadImageToServer(photoFile: File) {
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), photoFile)
        val imagePart = MultipartBody.Part.createFormData("image", photoFile.name, requestBody)

        ApiClient.apiService.uploadImage(imagePart, RequestBody.create("text/plain".toMediaTypeOrNull(), "someValue"))
            .enqueue(object : Callback<ApiResponse<DetectionHistoryItem>> {
                override fun onResponse(
                    call: Call<ApiResponse<DetectionHistoryItem>>,
                    response: Response<ApiResponse<DetectionHistoryItem>>
                ) {
                    if (response.isSuccessful) {
                        val detectResponse = response.body()
                        Toast.makeText(
                            requireContext(),
                            "Success: ${detectResponse?.data?.result}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<DetectionHistoryItem>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(requireContext(), "Camera initialization failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
