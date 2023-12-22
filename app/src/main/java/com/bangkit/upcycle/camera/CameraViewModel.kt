package com.bangkit.upcycle.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.upcycle.repository.ModelDataJson
import com.bangkit.upcycle.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CameraViewModel (private val repository: UserRepository) : ViewModel() {

    var isLoading: LiveData<Boolean> = repository.isLoading

    fun uploadRecycle(request: ModelDataJson) {
        viewModelScope.launch {
            repository.uploadRecycle(request)
        }
    }
    fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody
    ) {
        repository.uploadImage(file, description)
    }

}