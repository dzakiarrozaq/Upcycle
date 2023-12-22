package com.bangkit.upcycle

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.upcycle.camera.CameraFragment
import com.bangkit.upcycle.camera.CameraViewModel
import com.bangkit.upcycle.di.Injection
import com.bangkit.upcycle.login.LoginViewModel
import com.bangkit.upcycle.register.SignUpViewModel
import com.bangkit.upcycle.repository.UserRepository


class ViewModelFactory (
    private val repository: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(repository) as T
        }
//        }else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
//            return MapsViewModel(repository) as T
//        }
        throw IllegalArgumentException("Kelas ViewModel tidak dikenal")
    }
    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun clearInstance() {
            UserRepository.clearInstance()
            INSTANCE = null
        }

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}