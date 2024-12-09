package com.example.pesawit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.Article
import com.example.pesawit.ui.home.artikel.CreateArticleFragment
import com.example.pesawit.ui.home.artikel.EditArticleFragment
import com.example.pesawit.viewmodel.HomeViewModel
import com.example.pesawit.viewmodel.HomeViewModelFactory
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

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext()))
            .get(HomeViewModel::class.java)

        // Mendapatkan role pengguna dari arguments
        userRole = arguments?.getString("userRole")
        viewModel.userRole.value = userRole

        // Mengatur visibilitas tombol Create Article
        val btnCreateArticle: View? = view.findViewById(R.id.btn_create_article)
        btnCreateArticle?.visibility = if (userRole == "admin") View.VISIBLE else View.GONE
        btnCreateArticle?.setOnClickListener {
            onCreateArticle()
        }

        // Observasi data artikel
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            Log.d("HomeFragment", "Articles loaded: $articles")
            recyclerView.adapter = if (userRole == "admin") {
                AdminAdapter(articles, ::onEditArticle, ::onDeleteArticle)
            } else {
                UserAdapter(articles)
            }
        }

        // Memuat data artikel dari ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticles()
        }
    }

    // Navigasi ke CreateArticleFragment
    private fun onCreateArticle() {
        val createArticleFragment = CreateArticleFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, createArticleFragment)
            .addToBackStack(null)
            .commit()
    }

    // Navigasi ke EditArticleFragment
    private fun onEditArticle(article: Article) {
        Log.d("HomeFragment", "Edit article: ${article.title}")
        val editArticleFragment = EditArticleFragment().apply {
            arguments = Bundle().apply {
                putParcelable("article", article) // Mengirimkan artikel untuk diedit
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editArticleFragment)
            .addToBackStack(null)
            .commit()
    }

    // Logika penghapusan artikel
    private fun onDeleteArticle(article: Article) {
        Log.d("HomeFragment", "Delete article: ${article.title}")
        // Tambahkan logika penghapusan artikel di sini, misalnya menggunakan ViewModel
    }
}
