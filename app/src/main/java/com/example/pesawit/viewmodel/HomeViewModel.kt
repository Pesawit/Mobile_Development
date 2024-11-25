package com.example.pesawit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pesawit.data.response.Article

class HomeViewModel : ViewModel() {

    // MutableLiveData to hold the list of articles
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    // MutableLiveData to manage the role of the user
    private val _userRole = MutableLiveData<String>()
    val userRole: LiveData<String> = _userRole

    init {
        // Load dummy articles initially
        loadDummyArticles()
    }

    // Function to set the user role
    fun setUserRole(role: String) {
        _userRole.value = role
    }

    // Function to update the list of articles
    fun setArticles(newArticles: List<Article>) {
        _articles.value = newArticles
    }

    // Function to load dummy articles
    private fun loadDummyArticles() {
        _articles.value = listOf(
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
    }

    // Function to handle article editing
    fun editArticle(articleId: String, updatedArticle: Article) {
        _articles.value = _articles.value?.map {
            if (it.id == articleId) updatedArticle else it
        }
    }

    // Function to handle article deletion
    fun deleteArticle(articleId: String) {
        _articles.value = _articles.value?.filter { it.id != articleId }
    }
}