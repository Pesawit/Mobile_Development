package com.example.pesawit.data.response

import com.google.gson.annotations.SerializedName

data class ResponseAPI<T>(
    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: T?,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("success")
    val success: Boolean?
)