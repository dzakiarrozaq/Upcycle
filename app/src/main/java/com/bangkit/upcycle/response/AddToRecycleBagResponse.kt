package com.bangkit.upcycle.response

import com.google.gson.annotations.SerializedName

data class AddToRecycleBagResponse(

	@field:SerializedName("historyEntry")
	val historyEntry: HistoryEntry? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HistoryEntry(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("wasteImage")
	val wasteImage: String? = null,

	@field:SerializedName("recycledProduct")
	val recycledProduct: String? = null
)
