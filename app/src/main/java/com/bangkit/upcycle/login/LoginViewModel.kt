package com.bangkit.upcycle.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.upcycle.preferences.User
import com.bangkit.upcycle.repository.UserRepository
import com.bangkit.upcycle.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private var repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val response = repository.login(email, password)
            _loginResponse.value = response

            _isLoading.value = false
        }
    }

    suspend fun saveSession(user: User) {
        repository.saveSession(user)
    }
}




