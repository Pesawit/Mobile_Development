package com.example.pesawit.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Response(

	@field:SerializedName("Register")
	val registerRequest: Register? = null,

	@field:SerializedName("baseUrl")
	val baseUrl: String? = null,

	@field:SerializedName("UserProfile")
	val userProfile: UserProfile? = null,

	@field:SerializedName("DetectionHistory")
	val detectionHistory: DetectionHistory? = null,

	@field:SerializedName("Article")
	val article: Article? = null,

	@field:SerializedName("LoginRequest")
	val loginRequest: LoginResponse? = null,


) : Parcelable

@Parcelize
data class ApiResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@SerializedName("success")
	val success: Boolean?,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class Register(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("confirmPassword")
	val confirmPassword: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@SerializedName("success")
	val success: Boolean?
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("is_published")
	val isPublished: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("detectedAt")
	val detectedAt: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null
) : Parcelable

@Parcelize
data class UserProfile(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable

@Parcelize
data class LoginResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@SerializedName("success")
	val success: Boolean?
) : Parcelable

@Parcelize
data class DetectionHistory(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("detectedAt")
	val detectedAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

) : Parcelable

@Parcelize
data class Article(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("is_published")
	var isPublished: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	var title: String? = null,

	@field:SerializedName("content")
	var content: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null
) : Parcelable
