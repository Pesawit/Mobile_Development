package com.example.pesawit.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @field:SerializedName("message")
    val message: String? ,

    @SerializedName("data")
    val data: T? = null,

    @field:SerializedName("token")
    val token: String? ,

    @field:SerializedName("role")
    val role: String? ,

    @SerializedName("success")
    val success: Boolean?,

    @field:SerializedName("user")
    val user: User? = null
)
