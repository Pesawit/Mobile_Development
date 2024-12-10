package com.example.pesawit.ui.home.artikel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.databinding.FragmentArticleListBinding

class ArticleListFragment : Fragment() {

    private var _binding: FragmentArticleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        // Set listener untuk navigasi
        adapter = ArticleListAdapter { article ->
            val action =
                ArticleListFragmentDirections.actionArticleListFragmentToArticleDetailFragment(article)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupRecyclerView() {
        // Data dummy untuk testing
        val dummyArticles = listOf(
            ArticlesItem(
                id = "1",
                title = "Sample Article 1",
                author = "John Doe",
                content = "This is the content of article 1.",
                tags = listOf("Tag1", "Tag2"),
                createdAt = "2024-12-10"
            ),
            ArticlesItem(
                id = "2",
                title = "Sample Article 2",
                author = "Jane Doe",
                content = "This is the content of article 2.",
                tags = listOf("Tag3", "Tag4"),
                createdAt = "2024-12-11"
            )
        )
        adapter.submitList(dummyArticles)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
