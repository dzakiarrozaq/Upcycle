package com.bangkit.upcycle.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("token")
	val isSuccess: Boolean? = null,

	@field:SerializedName("token")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
