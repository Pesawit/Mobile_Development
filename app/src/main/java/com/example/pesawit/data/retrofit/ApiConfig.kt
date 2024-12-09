package com.example.pesawit.data.retrofit

import android.content.Context
import com.example.pesawit.utils.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "https://server-pesawit-469455376612.asia-southeast2.run.app/api/v1/"

    // Membuat OkHttpClient dengan dukungan Authorization header
    private fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = TokenManager.getToken(context) // Mendapatkan token dari TokenManager
                val request = chain.request().newBuilder()
                if (!token.isNullOrEmpty()) {
                    request.addHeader("Authorization", "Bearer $token") // Menambahkan Authorization Header
                }
                chain.proceed(request.build())
            }
            .connectTimeout(30, TimeUnit.SECONDS) // Atur timeout koneksi
            .readTimeout(30, TimeUnit.SECONDS)    // Atur timeout membaca data
            .writeTimeout(30, TimeUnit.SECONDS)   // Atur timeout menulis data
            .build()
    }

    // Membuat Retrofit instance
    fun provideApiService(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Gunakan URL API backend Anda
            .addConverterFactory(GsonConverterFactory.create()) // Mengonversi JSON ke data class
            .client(provideOkHttpClient(context)) // Menyertakan OkHttpClient
            .build()
            .create(ApiService::class.java)
    }
}
