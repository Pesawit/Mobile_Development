package com.example.pesawit.model

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val isPublished: String = "Unpublished"
) {
    val description: CharSequence? = null
    val date: CharSequence? = null
}
