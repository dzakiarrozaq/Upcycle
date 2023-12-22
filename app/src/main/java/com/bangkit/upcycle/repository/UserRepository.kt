package com.bangkit.upcycle.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.response.AddToRecycleBagResponse
import com.bangkit.upcycle.response.ErrorResponse
import com.bangkit.upcycle.response.LoginResponse
import com.bangkit.upcycle.response.RegisterResponse

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreferences,
) {
    private var _loginResponse = MutableLiveData<LoginResponse>()
    var loginResponse: MutableLiveData<LoginResponse> = _loginResponse

    var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    suspend fun registerUser(username: String, email: String, password: String): RegisterResponse {
        withContext(Dispatchers.Main) {
            _isLoading.value = true
        }

        val deferred = CompletableDeferred<RegisterResponse>()

        val requestBody = JsonObject().apply {
            addProperty("name", username)
            addProperty("email", email)
            addProperty("password", password)
        }

        try {
            val response = apiService.register(requestBody)

            withContext(Dispatchers.Main) {
                _isLoading.value = false
            }

            if (response.isSuccessful) {
                response.body()?.let { deferred.complete(it) }
            } else {
                val errorMessage = extractErrorMessage(response)
                Log.e("UserRepository", errorMessage)
            }
        } catch (t: Throwable) {
            val errorMessage = "Registration failed: ${t.message}"
            Log.e("UserRepository", errorMessage)

            withContext(Dispatchers.Main) {
                _isLoading.value = false
            }
        }

        return deferred.await()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        _isLoading.value = true

        val deferred = CompletableDeferred<LoginResponse>()

        val requestBody = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }

        val client = apiService.login(requestBody)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { deferred.complete(it) }
                } else {
                    val errorMessage = extractErrorMessage(response)
                    Log.e("UserRepository", errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val errorMessage = "Login failed: ${t.message}"
                Log.e("StoryRepository", errorMessage)
                _isLoading.value = false
            }
        })

        return deferred.await()
    }


    private fun extractErrorMessage(response: Response<*>): String {
        return try {
            val errorObject =
                Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            errorObject.message ?: "Registration failed: ${response.message()}"
        } catch (e: Exception) {
            "Registration failed: ${response.message()}"
        }
    }

    suspend fun saveSession(user: com.bangkit.upcycle.preferences.User) {
        pref.saveAuthToken(user)
    }

    fun getSession(): kotlinx.coroutines.flow.Flow<com.bangkit.upcycle.preferences.User> {
        return pref.getTokenKey()
    }

    private val _recyclingResponse = MutableLiveData<AddToRecycleBagResponse>()
    val recyclingResponse: LiveData<AddToRecycleBagResponse>
        get() = _recyclingResponse
    fun uploadRecycle(request: ModelDataJson) {
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(jsonMediaType, Gson().toJson(request))

        apiService.uploadRecycle(requestBody)
            .enqueue(object : Callback<AddToRecycleBagResponse> {
                override fun onResponse(
                    call: Call<AddToRecycleBagResponse>,
                    response: Response<AddToRecycleBagResponse>
                ) {
                    if (response.isSuccessful) {
                        _recyclingResponse.value = response.body()
                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<AddToRecycleBagResponse>, t: Throwable) {
                    // Handle network failure
                }
            })
    }
    fun uploadImage(file: MultipartBody.Part, name: RequestBody) {
        _isLoading.value = true
        val client = apiService.uploadImage(file, name)
        client.enqueue(object : Callback<AddToRecycleBagResponse> {
            override fun onResponse(
                call: Call<AddToRecycleBagResponse>,
                response: Response<AddToRecycleBagResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<AddToRecycleBagResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("StoryRepository", "error: ${t.message}")
            }
        })
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun clearInstance() {
            instance = null
        }

        fun getInstance(apiService: ApiService, userPreference: UserPreferences) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference).also { instance = it }
            }
    }

    suspend fun logout(): Unit {
        pref.clearToken()
    }
}