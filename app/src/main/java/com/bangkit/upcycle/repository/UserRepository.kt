package com.bangkit.upcycle.repository

import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.preferences.UserPreference
import com.bangkit.upcycle.response.RegisterResponse

class UserRepository(private val apiService: ApiService, private val pref: UserPreference) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
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

        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference).also { instance = it }
            }
    }
}