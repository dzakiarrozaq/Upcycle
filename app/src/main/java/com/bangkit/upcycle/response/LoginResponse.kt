package com.bangkit.upcycle.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("token")
	val token: String?
)
