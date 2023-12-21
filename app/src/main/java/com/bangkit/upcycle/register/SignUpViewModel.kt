package com.bangkit.upcycle.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.upcycle.repository.UserRepository
import com.bangkit.upcycle.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.registerUser(username, email, password)

                withContext(Dispatchers.Main) {
                    if (response.message != null) {
                        _message.postValue(response.message)
                    } else {
                        Log.e("SignUpViewModel", "Registrasi gagal: Response null or missing message")
                        _message.postValue("Unknown error")
                    }
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody?.message ?: "Unknown error"

                withContext(Dispatchers.Main) {
                    _message.postValue(errorMessage)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _message.value = null
    }
}