package com.example.pesawit.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.Article
import com.example.pesawit.ui.home.artikel.ArticleListAdapter
import com.example.pesawit.viewmodel.viewhome.HomeViewModel
import com.example.pesawit.viewmodel.viewhome.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel  // Menjadikan ini private
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

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireActivity().application))
            .get(HomeViewModel::class.java)

        // Get the user role from arguments or SharedPreferences
        userRole = arguments?.getString("userRole") ?:
                requireContext().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
                    .getString("userRole", null)

        // Set the user role in ViewModel
        viewModel.userRole.value = userRole

        // Show create article button if user is admin
        val btnCreateArticle: View? = view.findViewById(R.id.btn_create_article)
        btnCreateArticle?.visibility = if (userRole == "admin") View.VISIBLE else View.GONE
        btnCreateArticle?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createArticleFragment)
        }

        // Observe articles and update the RecyclerView
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            recyclerView.adapter = if (userRole == "admin") {
                AdminAdapter(articles, ::onEditArticle, ::onDeleteArticle, ::onReadMoreClick)
            } else {
                UserAdapter(articles, ::onReadMoreClick)
            }
        }



        // Fetch articles from API
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArticles()
        }
    }

    // Navigate to ArticleDetailFragment
    private fun onReadMoreClick(article: Article) {
        val action = HomeFragmentDirections.actionHomeFragmentToArticleDetailFragment(article)
        findNavController().navigate(action)
    }

    // Navigate to EditArticleFragment
    private fun onEditArticle(article: Article) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditArticleFragment(article)
        findNavController().navigate(action)
    }

    // Delete the article
    private fun onDeleteArticle(article: Article) {
        article.id?.let {
            viewModel.deleteArticle(it)
            Toast.makeText(requireContext(), "Article deleted", Toast.LENGTH_SHORT).show()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getArticles()  // Refresh the article list after deletion
            }
        }
    }
}
