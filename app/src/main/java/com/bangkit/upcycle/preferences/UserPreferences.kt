package com.bangkit.upcycle.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {


    suspend fun saveAuthToken(data: User) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = data.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getTokenKey(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[TOKEN_KEY].toString(),
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }
    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(NAME_KEY)
            preferences.remove(USER_ID)
            preferences[IS_LOGIN_KEY] = false
        }
    }
    companion object {

        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("userId")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}