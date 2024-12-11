package com.example.pesawit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.ui.home.artikel.ArticleDetailFragment
import com.example.pesawit.ui.home.artikel.CreateArticleFragment
import com.example.pesawit.ui.home.artikel.EditArticleFragment
import com.example.pesawit.viewmodel.HomeViewModel
import com.example.pesawit.viewmodel.viewhome.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewModel: HomeViewModel
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

        // Ambil userRole dari arguments atau SharedPreferences
        userRole = arguments?.getString("userRole")
        if (userRole == null) {
            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
            userRole = sharedPreferences.getString("userRole", null)
            Log.d("HomeFragment", "Retrieved userRole from SharedPreferences: $userRole")
        }

        if (userRole == null) {
            Log.e("HomeFragment", "userRole is null after checking SharedPreferences and arguments")
        } else {
            Log.d("HomeFragment", "Final userRole: $userRole")
        }

        viewModel.userRole.value = userRole

        // Tampilkan tombol create article jika role adalah admin
        val btnCreateArticle: View? = view.findViewById(R.id.btn_create_article)
        btnCreateArticle?.visibility = if (userRole == "admin") View.VISIBLE else View.GONE
        btnCreateArticle?.setOnClickListener {
            onCreateArticle()
        }

        // Observasi artikel dari ViewModel
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            recyclerView.adapter = if (userRole == "admin") {
                AdminAdapter(
                    articles,
                    ::onEditArticle,
                    ::onDeleteArticle,
                    ::onReadMoreClick
                )
            } else {
                UserAdapter(articles, ::onReadMoreClick)
            }
        }

        // Ambil artikel dari API
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

    private fun onReadMoreClick(article: ArticlesItem) {
        val articleDetailFragment = ArticleDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("article", article)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, articleDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
