package com.example.pesawit.data.retrofit

import com.example.pesawit.data.response.ApiResponse
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.data.response.DetectionHistoryItem
import com.example.pesawit.data.response.LoginResponse
import com.example.pesawit.data.response.Profile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun registerUser(@Body requestBody: Map<String, String>): Response<ApiResponse<Profile>>

    @POST("login")
    suspend fun loginUser(@Body requestBody: Map<String, String>): Response<ApiResponse<LoginResponse>>

    @GET("history")
    suspend fun getHistory(): Response<ApiResponse<List<DetectionHistoryItem>>>

    @GET("articles/{id}")
    suspend fun getArticleDetails(@Path("id") id: String): Response<ApiResponse<ArticlesItem>>

    @POST("articles")
    suspend fun createArticle(@Body article: ArticlesItem): Response<ApiResponse<ArticlesItem>>

    @GET("articles")
    suspend fun getArticles(): Response<ApiResponse<List<ArticlesItem>>>

    @Multipart
    @POST("api/v1/detect")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("someParam") requestBody: RequestBody
    ): Response<ApiResponse<DetectionHistoryItem>>
}
