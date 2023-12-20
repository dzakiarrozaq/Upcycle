package com.bangkit.upcycle.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("message")
	val message: String? = null
)

