package com.bangkit.upcycle.di

import android.content.Context
import com.bangkit.upcycle.apii.ApiConfig
import com.bangkit.upcycle.preferences.UserPreference
import com.bangkit.upcycle.preferences.dataStore
import com.bangkit.upcycle.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository = runBlocking  {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getTokenKey().first()
        val apiService = ApiConfig.getApiService(user.token)
        UserRepository.getInstance(apiService, pref)
    }
}