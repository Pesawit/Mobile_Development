package com.example.pesawit.ui.home

import android.content.Context
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
import com.example.pesawit.MainActivity
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.ui.home.artikel.ArticleDetailFragment
import com.example.pesawit.ui.home.artikel.CreateArticleFragment
import com.example.pesawit.ui.home.artikel.EditArticleFragment
import com.example.pesawit.viewmodel.viewhome.HomeViewModel
import com.example.pesawit.viewmodel.viewhome.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), MainActivity.UserRoleCallback {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewModel: HomeViewModel
    private var userRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the ViewModel using the factory
        val factory = HomeViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }
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

        // Observe the ViewModel's articles LiveData
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            // Update the adapter based on the userRole
            recyclerView.adapter = if (userRole.equals("admin", ignoreCase = true)) {
                AdminAdapter(articles, ::onEditArticle, ::onDeleteArticle, ::onReadMoreClick)
            } else {
                UserAdapter(articles, ::onReadMoreClick)
            }
        }

        // Fetch articles from the API
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticles()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Set the UserRoleCallback on the MainActivity
        if (context is MainActivity) {
            context.setUserRoleCallback(this)
        }
    }

    override fun onUserRoleReceived(userRole: String?) {
        this.userRole = userRole
        Log.d("HomeFragment", "Received userRole (callback): $userRole")

        // Update the ViewModel's userRole
        viewModel.userRole.value = userRole

        // Update UI elements based on userRole
        val btnCreateArticle: View? = view?.findViewById(R.id.btn_create_article)
        btnCreateArticle?.visibility = if (userRole.equals("admin", ignoreCase = true)) View.VISIBLE else View.GONE
        btnCreateArticle?.setOnClickListener {
            onCreateArticle()
        }

        // Refresh the adapter to reflect the userRole changes
        recyclerView.adapter?.notifyDataSetChanged()
    }


    private fun onCreateArticle() {
        val createArticleFragment = CreateArticleFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, createArticleFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onItemClick(article: ArticlesItem) {
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
