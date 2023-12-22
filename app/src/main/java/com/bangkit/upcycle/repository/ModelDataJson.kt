package com.bangkit.upcycle.repository

import okhttp3.MultipartBody

data class ModelDataJson(
    val wasteImage: MultipartBody.Part,
//    val wasteImage: String,
    val recycledProduct: String
)
