package com.bangkit.upcycle.register

import androidx.lifecycle.ViewModel
import com.bangkit.upcycle.repository.UserRepository
import com.bangkit.upcycle.response.RegisterResponse

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name, email, password)
    }
}