package com.bangkit.upcycle.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.response.ErrorResponse
import com.bangkit.upcycle.response.LoginResponse
import com.bangkit.upcycle.response.RegisterResponse
import com.google.gson.Gson
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

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _loginResponse.value = response.body()
                } else {
                    val errorMessage = extractErrorMessage(response)
                    Log.e("StoryRepository", errorMessage)
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val errorMessage = "Login failed: ${t.message}"
                Log.e("Trash Repository", errorMessage)
                _isLoading.value = false
            }
        })
    }

    private fun extractErrorMessage(response: Response<*>): String {
        return try {
            val errorObject = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            errorObject.message ?: "Login failed: ${response.message()}"
        } catch (e: Exception) {
            "Login failed: ${response.message()}"
        }
    }

    suspend fun saveSession(user: com.bangkit.upcycle.preferences.User) {
        pref.saveAuthToken(user)
    }
    fun getSession(): kotlinx.coroutines.flow.Flow<com.bangkit.upcycle.preferences.User>{
        return pref.getTokenKey()
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