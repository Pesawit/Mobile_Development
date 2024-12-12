package com.example.pesawit.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://server-pesawit-469455376612.asia-southeast2.run.app/api/v1/"


    private fun getAuthToken(): String {
        // Ganti "YOUR_ACCESS_TOKEN" dengan token aslimu atau dinamis dari DataStore/Session
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjQzYjI1NWIxLWQwN2UtNDE5Zi04MzdiLTI0ZTcyZTA1NGE3ZiIsIm5hbWUiOiJBZG1pbiBQZXNhd2l0Iiwicm9sZSI6ImFkbWluIiwiaWF0IjoxNzM0MDIwMDYzLCJleHAiOjE3MzQ2MjQ4NjN9.2hR5uiteaXVCB_jfl105DHHbK4YBVTlUXMSqwTbz87s"
    }

    // Interceptor untuk menyisipkan Authorization Header
    private val authInterceptor = Interceptor { chain ->
        val token = getAuthToken()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }

    // OkHttpClient dengan interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Fungsi untuk mendapatkan Retrofit client
    fun getClient(): Retrofit {
        return retrofit
    }
}
