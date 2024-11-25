package com.example.pesawit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.Article

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>

    // Sample data
    private val dummyArticles: List<Article>
        get() = listOf(
            Article(
                id = "1",
                title = "Judul Artikel 1",
                createdAt = "2024-11-19",
                content = "Ini adalah deskripsi singkat untuk artikel pertama.",
                author = "Author 1",
                isPublished = true,
                updatedAt = "2024-11-20",
                imageUrl = "url/to/image1",
                tags = listOf("Tag1", "Tag2")
            ),
            Article(
                id = "2",
                title = "Judul Artikel 2",
                createdAt = "2024-11-18",
                content = "Deskripsi artikel kedua yang cukup menarik.",
                author = "Author 2",
                isPublished = false,
                updatedAt = "2024-11-19",
                imageUrl = "url/to/image2",
                tags = listOf("Tag3", "Tag4")
            ),
            Article(
                id = "3",
                title = "Judul Artikel 3",
                createdAt = "2024-11-17",
                content = "Artikel ini membahas sesuatu yang sangat penting.",
                author = "Author 3",
                isPublished = true,
                updatedAt = "2024-11-18",
                imageUrl = "url/to/image3",
                tags = listOf("Tag5", "Tag6")
            )
        )

    private var userRole: String? = null  // Will be set from MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Retrieve role passed from MainActivity
        userRole = arguments?.getString("userRole")

        recyclerView = view.findViewById(R.id.rv_articles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Choose adapter based on user role
        adapter = if (userRole == "admin") {
            AdminAdapter(dummyArticles, ::onEditArticle, ::onDeleteArticle)
        } else {
            UserAdapter(dummyArticles)
        }

        recyclerView.adapter = adapter
        return view
    }

    private fun onEditArticle(article: Article) {
        // Implement editing logic here
    }

    private fun onDeleteArticle(article: Article) {
        // Implement delete logic here
    }
}
