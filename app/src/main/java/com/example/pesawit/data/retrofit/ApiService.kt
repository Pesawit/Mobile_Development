package com.example.pesawit.data.retrofit

import com.example.pesawit.data.response.ApiResponse
import com.example.pesawit.data.response.DataItem
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.data.response.Article
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun registerUser(@Body requestBody: Map<String, String>): Response<ApiResponse<ResponseItem>>

    @POST("login")
    suspend fun loginUser(@Body requestBody: Map<String, String>): Response<ApiResponse<ResponseItem>>

    @GET("history")
    suspend fun getHistory(): Response<ApiResponse<List<DataItem>>>

    @Headers("Authorization: Bearer <token>")
    @GET("articles/{id}")
    suspend fun getArticleDetails(@Path("id") id: String): Response<ApiResponse<Article>>

    @POST("articles")
    suspend fun createArticle(@Body article: Article): Response<ApiResponse<Article>>

    @GET("articles")
    suspend fun getArticles(): Response<ApiResponse<List<Article>>>



}
