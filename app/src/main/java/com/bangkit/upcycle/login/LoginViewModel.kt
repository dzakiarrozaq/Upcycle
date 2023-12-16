package com.bangkit.upcycle.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.repository.UserRepository
import kotlinx.coroutines.launch



class LoginViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login() {
        val emailValue = email.value.orEmpty()
        val passwordValue = password.value.orEmpty()
        showLoading()
        viewModelScope.launch {
            try {
                val response = userRepository.login(emailValue, passwordValue)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        userRepository.saveSession(loginResponse) // Assuming "saveSession" takes "LoginResponse"
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Error("Login response body is null.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _loginState.value = LoginState.Error(errorBody ?: "Unknown error.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "An unexpected error occurred.")
            } finally {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        // Implement loading UI updates based on your view framework
    }

    private fun hideLoading() {
        // Implement hiding loading UI based on your view framework
    }

    sealed class LoginState {
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}


