package com.example.pesawit.ui.camera

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class PredictionFragment : Fragment() {

    private lateinit var binding: FragmentPredictionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil imageUri dari Safe Args
        val args = PredictionFragmentArgs.fromBundle(requireArguments())
        val imageUri = Uri.parse(args.imageUri)

        // Menampilkan gambar dan hasil prediksi
        binding.ivPredictedImage.setImageURI(imageUri)

        // Anda dapat mengganti teks ini dengan hasil prediksi yang sebenarnya
        binding.tvPredictedResult.text = "Hasil Prediksi: Kanker atau Tidak Kanker" // Replace with actual result
    }
}
