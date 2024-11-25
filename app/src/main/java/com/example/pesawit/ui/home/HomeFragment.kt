package com.example.pesawit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.model.Article

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    private val dummyArticles: List<Article>
        get() = listOf(
            Article(
                "1",
                "Judul Artikel 1",
                "2024-11-19",
                "Ini adalah deskripsi singkat untuk artikel pertama."
            ),
            Article("2", "Judul Artikel 2", "2024-11-18", "Deskripsi artikel kedua yang cukup menarik."),
            Article("3", "Judul Artikel 3", "2024-11-17", "Artikel ini membahas sesuatu yang sangat penting.")
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Ubah id menjadi rv_articles sesuai dengan yang ada di XML
        recyclerView = view.findViewById(R.id.rv_articles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserAdapter(dummyArticles)
        recyclerView.adapter = adapter

        return view
    }
}