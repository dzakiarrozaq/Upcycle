package com.bangkit.upcycle.apii

import com.bangkit.upcycle.response.LoginResponse
import com.bangkit.upcycle.response.RegisterResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST



interface ApiService {
    @POST("register")
    suspend fun register(
        @Body requestBody: JsonObject
    ): Response<RegisterResponse>

    @POST("login")
    fun login(
        @Body requestBody: JsonObject
    ): Call<LoginResponse>

}