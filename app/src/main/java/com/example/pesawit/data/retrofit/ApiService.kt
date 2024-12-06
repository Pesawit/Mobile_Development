package com.example.pesawit.data.retrofit

import com.example.pesawit.data.response.ApiResponse
import com.example.pesawit.data.response.DataItem
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.data.response.Article
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun registerUser(@Body requestBody: Map<String, String>): Response<ApiResponse<ResponseItem>> // Gunakan ResponseItem

    @POST("login")
    suspend fun loginUser(@Body requestBody: Map<String, String>): Response<ApiResponse<ResponseItem>> // Gunakan ResponseItem

    @GET("history")
    suspend fun getHistory(): Response<ApiResponse<List<DataItem>>> // Tetap gunakan DataItem

    @GET("articles/{id}")
    suspend fun getArticleDetails(@Path("id") id: String): Response<ApiResponse<Article>> // Gunakan Article
}
