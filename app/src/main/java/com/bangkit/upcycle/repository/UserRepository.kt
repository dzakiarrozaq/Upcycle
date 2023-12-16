package com.bangkit.upcycle.repository

import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.response.LoginResponse
import com.bangkit.upcycle.response.RegisterResponse
import retrofit2.Call

class UserRepository(private val apiService: ApiService, private val pref: UserPreferences) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email, password)
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