package com.example.pesawit.ui.camera

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pesawit.databinding.FragmentPredictionBinding

class PredictionFragment : Fragment() {

    private lateinit var binding: FragmentPredictionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = PredictionFragmentArgs.fromBundle(requireArguments())
        val imageUri = Uri.parse(args.imageUri)

        binding.ivPredictedImage.setImageURI(imageUri)
        binding.tvPredictedResult.text = "Hasil Prediksi: [Hasil Deteksi]" // Replace with actual prediction
    }
}
