package com.example.pesawit.viewmodel.viewhome

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel(private val context: Context) : ViewModel() {

    private val _articles = MutableLiveData<List<ArticlesItem>>()
    val articles: LiveData<List<ArticlesItem>> = _articles

    val userRole = MutableLiveData<String?>()

    private val apiService = ApiConfig.provideApiService(context)

    init {
        _articles.postValue(loadDummyArticles()) // Initially load dummy data if needed
    }

    fun getArticles() {
        viewModelScope.launch {
            try {
                val response = apiService.getArticles()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        _articles.postValue(apiResponse.data ?: emptyList())
                    } else {
                        Log.e("HomeViewModel", "Error fetching articles: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("HomeViewModel", "Error fetching articles: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception fetching articles: ${e.message}")
            }
        }
    }

    fun createArticle(article: ArticlesItem) {
        viewModelScope.launch {
            try {
                val response = apiService.createArticle(article)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        apiResponse.data?.let { createdArticle ->
                            val updatedArticles = _articles.value.orEmpty() + createdArticle
                            _articles.postValue(updatedArticles)
                            Log.d("HomeViewModel", "Article created successfully: ${createdArticle.title}")
                        }
                    } else {
                        Log.e("HomeViewModel", "Failed to create article: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("HomeViewModel", "Failed to create article: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception creating article: ${e.message}")
            }
        }
    }

    fun editArticle(articleId: String, updatedArticle: ArticlesItem) {
        _articles.value = _articles.value?.map {
            if (it.id == articleId) updatedArticle else it
        }
    }

    fun deleteArticle(articleId: String) {
        _articles.value = _articles.value?.filter { it.id != articleId }
    }

    private fun loadDummyArticles(): List<ArticlesItem> {
        // Optionally use dummy data for initial loading or testing
        return listOf(
            ArticlesItem(
                id = "1",
                title = "Judul Artikel 1",
                content = "Ini adalah deskripsi singkat untuk artikel pertama.",
                author = "Author 1",
                isPublished = true,
                createdAt = "2024-11-19",
                updatedAt = "2024-11-20",
                image = "url/to/image1",
                tags = listOf("Tag1", "Tag2")
            )
        )
    }
}
