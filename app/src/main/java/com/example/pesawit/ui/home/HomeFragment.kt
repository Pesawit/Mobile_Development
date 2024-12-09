package com.example.pesawit.ui.home

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.ui.home.artikel.CreateArticleFragment
import com.example.pesawit.ui.home.artikel.EditArticleFragment
import com.example.pesawit.viewmodel.viewhome.HomeViewModel
import com.example.pesawit.viewmodel.viewhome.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel
    private var userRole: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext()))
            .get(HomeViewModel::class.java)

        userRole = arguments?.getString("userRole")
        viewModel.userRole.value = userRole

        val btnCreateArticle: View? = view.findViewById(R.id.btn_create_article)
        btnCreateArticle?.visibility = if (userRole == "admin") View.VISIBLE else View.GONE
        btnCreateArticle?.setOnClickListener {
            onCreateArticle()
        }

        // Observe changes to the articles
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            recyclerView.adapter = if (userRole == "admin") {
                AdminAdapter(articles, ::onEditArticle, ::onDeleteArticle)
            } else {
                UserAdapter(articles)
            }
        }

        // Fetch articles from API
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticles()
        }
    }

    private fun onCreateArticle() {
        val createArticleFragment = CreateArticleFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, createArticleFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onEditArticle(article: ArticlesItem) {
        val editArticleFragment = EditArticleFragment().apply {
            arguments = Bundle().apply {
                putParcelable("article", article)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editArticleFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onDeleteArticle(article: ArticlesItem) {
        // Handle article deletion
    }
}
