package com.example.pesawit.data.retrofit

import com.example.pesawit.data.response.ApiResponse
import com.example.pesawit.data.response.ResponseAPI
import com.example.pesawit.data.response.Article
import com.example.pesawit.data.response.DetectionHistory
import com.example.pesawit.data.response.LoginResponse // Menggunakan LoginRequest
import com.example.pesawit.data.response.Register// Menggunakan RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun registerUser(@Body requestBody: Map<String, String>): Response<ResponseAPI<Register>> // Menggunakan RegisterRequest
    @POST("login")
    suspend fun loginUser(
        @Body requestBody: Map<String, String>): Response<ResponseAPI<LoginResponse>>

    @GET("history")
    suspend fun getHistory(): Response<ResponseAPI<List<DetectionHistory>>>

    @GET("articles/{id}")
    suspend fun getArticleDetails(@Path("id") id: String): Response<ResponseAPI<Article>>

    @POST("articles")
    suspend fun createArticle(@Body article: Article): Response<ResponseAPI<Article>>

    @GET("articles")
    suspend fun getArticles(): Response<ResponseAPI<List<Article>>>

    @DELETE("articles/{id}")
    suspend fun deleteArticle(@Path("id") articleId: String): Response<ApiResponse>

    @Multipart
    @POST("articles/createWithImage")
    suspend fun createArticleWithImage(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ApiResponse>

    @Multipart
    @POST("detect")  // Your actual API endpoint here
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("someParam") requestBody: RequestBody
    ): Response<DetectionHistory>





}