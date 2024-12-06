package com.example.pesawit.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pesawit.data.response.DataItem
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = historyAdapter

        loadHistoryData()
    }

    private fun loadHistoryData() {
        // TODO: Implement logic to load history data from ResponseItem
        val dummyData = listOf(
            ResponseItem(
                data = DataItem(
                    image = "https://example.com/image1.jpg",
                    title = "Hasil Prediksi: Bunga"
                )
            ),
            ResponseItem(
                data = DataItem(
                    image = "https://example.com/image2.jpg",
                    title = "Hasil Prediksi: Mobil"
                )
            )
        )
        historyAdapter.submitList(dummyData)
    }
}