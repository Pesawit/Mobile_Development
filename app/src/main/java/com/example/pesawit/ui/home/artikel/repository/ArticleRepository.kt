package com.example.pesawit.ui.home.artikel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pesawit.data.response.ArticlesItem

class ArticleRepository {

    fun getArticles(): LiveData<List<ArticlesItem>> {
        val articles = MutableLiveData<List<ArticlesItem>>()
        articles.value = listOf(
            ArticlesItem(
                id = "1",
                title = "Title 1",
                author = "Author 1",
                content = "Content 1",
                tags = listOf("Tag1", "Tag2"),
                createdAt = "2024-12-10",
                isPublished = true // Sesuaikan dengan tipe data Boolean?
            ),
            ArticlesItem(
                id = "2",
                title = "Title 2",
                author = "Author 2",
                content = "Content 2",
                tags = listOf("Tag3", "Tag4"),
                createdAt = "2024-12-11",
                isPublished = false // Sesuaikan dengan tipe data Boolean?
            )
        )
        return articles
    }
}