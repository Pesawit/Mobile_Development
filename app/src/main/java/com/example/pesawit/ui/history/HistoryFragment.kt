package com.example.pesawit.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pesawit.data.response.History
import com.example.pesawit.databinding.FragmentHistoryBinding
import com.example.pesawit.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

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

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        historyAdapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        loadHistoryData()
    }

    private fun loadHistoryData() {
        val dummyData = listOf(
            History(
                id = "1",
                image = "https://via.placeholder.com/150",
                result = "Prediksi: White Scale",
                recommendation = "Rekomendasi: Gunakan insektisida dengan bahan aktif imidakloprid.",
                createdAt = "2024-12-09"
            ),
            History(
                id = "2",
                image = "https://via.placeholder.com/150",
                result = "Prediksi: Brown Scale",
                recommendation = "Rekomendasi: Lakukan pemupukan dengan NPK secara berkala.",
                createdAt = "2024-12-08"
            )
        )

        // Simpan data ke ViewModel
        dummyData.forEach { historyViewModel.addHistoryItem(it) }

        // Tampilkan data dari ViewModel
        historyAdapter.submitList(historyViewModel.getHistoryItems())
    }
}
