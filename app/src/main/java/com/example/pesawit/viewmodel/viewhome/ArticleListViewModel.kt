package com.example.pesawit.viewmodel.viewhome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.Article
import com.example.pesawit.ui.home.artikel.repository.ArticleRepository

class ArticleListViewModel(private val repository: ArticleRepository) : ViewModel() {

    val articles: LiveData<List<Article>> = repository.getArticles()
}
