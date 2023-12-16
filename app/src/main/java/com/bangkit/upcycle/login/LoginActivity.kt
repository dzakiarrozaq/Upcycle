package com.bangkit.upcycle.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.upcycle.R
import com.bangkit.upcycle.ViewModelFactory
import com.bangkit.upcycle.databinding.ActivityLoginBinding
import com.bangkit.upcycle.databinding.ActivitySignUpBinding
import com.bangkit.upcycle.register.SignUpViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBarRegister.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.loginButton.isEnabled = !isLoading
    }
}