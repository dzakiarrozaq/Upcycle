package com.bangkit.upcycle.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.upcycle.repository.UserRepository
import com.bangkit.upcycle.response.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.registerUser(username, email, password)

                if (user != null) {
                    handleSuccessfulRegistration(user)
                }
            } catch (e: Exception) {
                handleFailedRegistration(e.message)
            }
        }
    }
    private fun handleSuccessfulRegistration(user: User) {
    }

    private fun handleFailedRegistration(errorMessage: String?) {
    }
}