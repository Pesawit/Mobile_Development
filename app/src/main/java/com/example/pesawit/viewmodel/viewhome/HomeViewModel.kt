package com.example.pesawit.viewmodel.viewhome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pesawit.data.response.Article
import com.example.pesawit.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Declare apiService once
    private val apiService = ApiConfig.provideApiService(application)

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    val userRole = MutableLiveData<String?>()

    init {
        _articles.postValue(loadDummyArticles()) // Initially load dummy data if needed
    }

    // Function to fetch articles from API
    fun getArticles() {
        viewModelScope.launch {
            try {
                // Operasi jaringan dijalankan di background thread
                val response = apiService.getArticles()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        // Pastikan data artikel diperbarui di UI
                        _articles.postValue(apiResponse.data ?: emptyList())
                        Log.d("HomeViewModel", "Articles fetched successfully")
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

    // Function to create an article
    @Suppress("unused")
    fun createArticle(article: Article) {
        viewModelScope.launch {
            try {
                val response = apiService.createArticle(article)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        apiResponse.data?.let { createdArticle ->
                            // Tambahkan artikel baru ke daftar artikel yang ada
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

    // Function to create an article with an image
    fun createArticleWithImage(title: String, content: String, imageFile: File) {
        viewModelScope.launch {
            try {
                if (!imageFile.exists()) {
                    Log.e("HomeViewModel", "Image file does not exist: ${imageFile.absolutePath}")
                    return@launch
                }
                if (!imageFile.canRead()) {
                    Log.e("HomeViewModel", "Cannot read image file: ${imageFile.absolutePath}")
                    return@launch
                }

                val titlePart = title.toRequestBody("text/plain".toMediaType())
                val contentPart = content.toRequestBody("text/plain".toMediaType())
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaType())
                )

                val response = apiService.createArticleWithImage(titlePart, contentPart, imagePart)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        apiResponse.data?.let { createdArticle ->
                            val updatedArticles = _articles.value.orEmpty() + createdArticle
                            _articles.postValue(updatedArticles as List<Article>?)
                            Log.d("HomeViewModel", "Article created successfully with image")
                        }
                    } else {
                        Log.e("HomeViewModel", "API responded but failed: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("HomeViewModel", "HTTP error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception creating article with image: ${e.message}")
            }
        }
    }
    // Function to edit an article
    fun editArticle(articleId: String, updatedArticle: Article) {
        _articles.value = _articles.value?.map {
            if (it.id == articleId) updatedArticle else it
        }
    }

    // Function to delete an article
    fun deleteArticle(articleId: String) {
        viewModelScope.launch {
            try {
                // Operasi penghapusan artikel dilakukan di background thread
                val response = apiService.deleteArticle(articleId)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        // Menghapus artikel yang sesuai dengan ID
                        _articles.value = _articles.value?.filter { it.id != articleId }
                        Log.d("HomeViewModel", "Article deleted successfully: $articleId")
                    } else {
                        Log.e("HomeViewModel", "Failed to delete article: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("HomeViewModel", "Failed to delete article: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception deleting article: ${e.message}")
            }
        }
    }

    // Dummy articles for initial loading
    private fun loadDummyArticles(): List<Article> {
        return listOf(
            Article(
                id = "1",
                title = "Judul Artikel 1",
                content = "Ini adalah deskripsi singkat untuk artikel pertama.",
                author = "Author 1",
                isPublished = true,
                createdAt = "2024-11-19",
                updatedAt = "2024-11-20",
                image = "https://via.placeholder.com/150",
                tags = listOf("Tag1", "Tag2")
            )
        )
    }
}
