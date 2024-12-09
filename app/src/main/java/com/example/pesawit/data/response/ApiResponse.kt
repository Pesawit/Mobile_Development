package com.example.pesawit.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @field:SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("role")
    val role: String? = null,

    @SerializedName("success")
    val success: Boolean
)
