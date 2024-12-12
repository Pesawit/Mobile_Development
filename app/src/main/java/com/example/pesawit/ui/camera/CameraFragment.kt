package com.example.pesawit.ui.camera

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.pesawit.databinding.FragmentCameraBinding
import com.example.pesawit.viewmodel.CameraViewModel
import com.example.pesawit.viewmodel.PredictionViewModel
import com.google.common.util.concurrent.ListenableFuture
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private val predictionViewModel: PredictionViewModel by viewModels()
    private val cameraViewModel: CameraViewModel by viewModels()
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var capturedFile: File? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }

        // Handle button clicks
        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnRetryCapture.setOnClickListener { resetCamera() }
        binding.btnAnalyze.setOnClickListener { navigateToPrediction() }
        binding.btnUpload.setOnClickListener { uploadPhoto() }

        // Observe the loading state to show or hide the loading indicator
        predictionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        updateUIAfterCapture()
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
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(requireContext(), "Failed to initialize camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Gunakan CameraViewModel untuk membuat file
        val file = cameraViewModel.createImageFile(requireContext())
        capturedFile = file

        Log.d("CameraFragment", "Captured file path: ${file.absolutePath}")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                    capturedFile = null
                    updateUIAfterCapture()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    if (file.exists() && file.length() > 0) {
                        binding.ivUploadedImage.visibility = View.VISIBLE
                        binding.ivUploadedImage.setImageURI(Uri.fromFile(file))

                        binding.viewFinder.visibility = View.GONE
                        binding.llActionButtons.visibility = View.VISIBLE

                        Log.d("CameraFragment", "Image saved successfully. File size: ${file.length()} bytes")
                    } else {
                        Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
                        capturedFile = null
                        Log.e("CameraFragment", "Image file does not exist or is empty")
                    }
                    updateUIAfterCapture()
                }
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted to use camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun resetCamera() {
        binding.ivUploadedImage.visibility = View.GONE
        binding.viewFinder.visibility = View.VISIBLE
        binding.llActionButtons.visibility = View.GONE
        capturedFile = null
        updateUIAfterCapture()
        startCamera()
    }

    private fun navigateToPrediction() {
        capturedFile?.let { file ->
            val action = CameraFragmentDirections.actionCameraFragmentToPredictionFragment(
                imageUri = Uri.fromFile(file).toString()
            )
            findNavController().navigate(action)
        } ?: Toast.makeText(requireContext(), "Please capture a photo first", Toast.LENGTH_SHORT).show()
    }

    private fun uploadPhoto() {
        capturedFile?.let { file ->
            val imageUri = Uri.fromFile(file)
            val imagePart = createImagePart(imageUri)
            imagePart?.let {
                predictionViewModel.uploadImage(it)
                Toast.makeText(requireContext(), "Uploading image...", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(requireContext(), "File image tidak valid", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(requireContext(), "Please capture a photo first", Toast.LENGTH_SHORT).show()
    }


    private fun createImagePart(uri: Uri): MultipartBody.Part? {
        val filePath = uri.path
        val file = filePath?.let { File(it) }

        // Pastikan file ada
        if (file != null && file.exists()) {
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("image", file.name, requestBody)
        }
        return null
    }


    private fun updateUIAfterCapture() {
        val photoTaken = capturedFile != null && capturedFile!!.exists()
        binding.btnUpload.isEnabled = photoTaken
        binding.btnAnalyze.isEnabled = photoTaken
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}