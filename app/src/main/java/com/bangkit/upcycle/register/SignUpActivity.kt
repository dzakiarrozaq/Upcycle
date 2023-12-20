package com.bangkit.upcycle.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.upcycle.ViewModelFactory
import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.databinding.ActivitySignUpBinding
import com.bangkit.upcycle.login.LoginActivity
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.repository.UserRepository
import com.bangkit.upcycle.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            register()
        }
        binding.signin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.message.observe(this, Observer { message ->
            message?.let {
                runOnUiThread {
                    showLoading(false)
                    showToast("Registrasi berhasil: $it")
                }
                // Handle other UI actions if needed
            } ?: run {
                runOnUiThread {
                    showLoading(false)
                    Log.e("RegisterActivity", "Registrasi gagal: Message null")
                }
            }
        })
    }

    private fun register() {
        if (validateFields()) {
            val name = binding.tvRegisterName.text.toString().trim()
            val email = binding.tvRegisterEmail.text.toString().trim()
            val password = binding.tvRegisterPassword.text.toString().trim()

            viewModel.registerUser(name, email, password)
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            when {
                tvRegisterName.text.isNullOrBlank() -> {
                    tvRegisterName.error = "Kolom Nama Tidak Boleh Kosong"
                    tvRegisterName.requestFocus()
                    return false
                }
                tvRegisterEmail.text.isNullOrBlank() -> {
                    tvRegisterEmail.error = "Kolom Email Tidak Boleh Kosong"
                    tvRegisterEmail.requestFocus()
                    return false
                }
                tvRegisterPassword.text.isNullOrBlank() -> {
                    tvRegisterPassword.error = "Kolom Password Tidak Boleh Kosong"
                    tvRegisterPassword.requestFocus()
                    return false
                }
                else -> return true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarRegister.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.signupButton.isEnabled = !isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}