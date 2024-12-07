package com.example.pesawit.data.retrofit

import android.content.Context
import com.example.pesawit.utils.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "https://server-pesawit-469455376612.asia-southeast2.run.app/api/v1/"

    private fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = TokenManager.getToken(context)
                val request = chain.request().newBuilder()
                if (!token.isNullOrEmpty()) {
                    request.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(request.build())
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun provideApiService(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(context))
            .build()
            .create(ApiService::class.java)
    }
}
