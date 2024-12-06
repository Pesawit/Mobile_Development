package com.example.pesawit.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: T? = null
)


