package com.bangkit.upcycle.repository

data class PredictionData(
    val label: String,
    val image: ImageData,
)

data class ImageData(
    val url: String,
    val localPath: String
)
