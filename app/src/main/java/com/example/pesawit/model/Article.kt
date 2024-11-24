package com.example.pesawit.model

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val isPublished: String
) {
    val description: CharSequence? = null
    val date: CharSequence? = null
}
