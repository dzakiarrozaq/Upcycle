package com.bangkit.upcycle.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.upcycle.preferences.User
import com.bangkit.upcycle.repository.UserRepository
import com.bangkit.upcycle.response.LoginResponse

class LoginViewModel(private var repository: UserRepository) : ViewModel() {

    val isLoading: LiveData<Boolean>
        get() = repository.isLoading

    val loginResponse: MutableLiveData<LoginResponse>
        get() = repository.loginResponse

//    val isLoading: LiveData<Boolean>
//        get() = repository.isLoading

//    val loginResponse: LiveData<LoginResponse>
//        get() = repository.loginResponse

    fun login(email: String, password: String) {
        repository.login(email, password)
    }

    suspend fun saveSession(user: User) {
        repository.saveSession(user)
    }

}


